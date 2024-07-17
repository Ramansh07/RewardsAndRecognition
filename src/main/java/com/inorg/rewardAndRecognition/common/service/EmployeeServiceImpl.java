package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.entity.EmployeeRoleMappingEntity;
import com.inorg.rewardAndRecognition.config.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.config.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRoleMappingRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRoleMappingRepository employeeRoleMappingRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;

    @Override
    public List<EmployeeDTO> findAllActiveEmployees() throws ResourceNotFoundException {
        Optional<List<EmployeeEntity>> activeEmployees = employeeRepository.findActiveEmployees();
        if (activeEmployees.isEmpty()) {
            throw new ResourceNotFoundException("No active employees found");
        }
        List<EmployeeEntity>temp = activeEmployees.get();

        return temp.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public EmployeeDTO findActiveEmployeeById(String id) throws ResourceNotFoundException, InvalidRequest {
        if (id == null || id.isEmpty()) {
            throw new InvalidRequest("Employee ID cannot be null or empty");
        }
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findActiveEmployeeById(id);
        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException("No active employee found with ID: " + id);
        }

        return mapEntityToDTO(optionalEmployeeEntity.get());
    }

    @Override
    public EmployeeDTO findActiveEmployeeByEmail(String email) throws ResourceNotFoundException, InvalidRequest {
        if (email == null || email.isEmpty()) {
            throw new InvalidRequest("Employee email cannot be null or empty");
        }
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findActiveEmployeeByEmail(email);
        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException("No active employee found with email: " + email);
        }
        return mapEntityToDTO(optionalEmployeeEntity.get());
    }

    @Override
    @Transactional
    public List<EmployeeDTO> putRoles(String adminId, List<SetRoleDTO> roleDTOs) throws Exception {
        EmployeeDTO adminObject = findActiveEmployeeByEmail(adminId);
        if(adminObject.getRole()<rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("you are not authorised to change the roles");
        }

        List<String> employeeIds = roleDTOs.stream()
                .map(SetRoleDTO::getEmployeeId)
                .collect(Collectors.toList());

        for(String id: employeeIds){
            if(Objects.equals(id, adminObject.getEmpId())){
                throw new NoAuthorisationException("You are not authorised to change your own role");
            }
        }

        List<EmployeeEntity> employees = employeeRepository.findActiveEmployeesByIds(employeeIds);
        if (employees.size() != employeeIds.size()) {
            throw new ResourceNotFoundException("One or more employees not found");
        }

        for (EmployeeEntity employee : employees) {
            for (SetRoleDTO roleDTO : roleDTOs) {
                if (employee.getEmpId().equals(roleDTO.getEmployeeId())) {
                    employee.setRole(roleDTO.getRole());
                    break;
                }
            }
        }

        List<EmployeeEntity> updatedEmployees = employeeRepository.saveAll(employees);
        return updatedEmployees.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO postDescription(String userId, SetDescriptionDTO descriptionDTO) throws Exception{
        EmployeeDTO employeeObject = findActiveEmployeeByEmail(userId);
        String id = employeeObject.getEmpId();
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findActiveEmployeeById(id);
        if (optionalEmployeeEntity.isEmpty()) {
            throw new ResourceNotFoundException("No active employee found with ID: " + id);
        }
        EmployeeEntity employeeEntity = optionalEmployeeEntity.get();
        employeeEntity.setDescription(descriptionDTO.getDescription());
        EmployeeEntity updatedEmployee = employeeRepository.save(employeeEntity);
        return mapEntityToDTO(updatedEmployee);
    }

    @Override
    public List<EmployeeRoleMappingEntity> getEmployeeRoleMapping() throws Exception {
        return  employeeRoleMappingRepository.findAll();
    }




    private EmployeeDTO mapEntityToDTO(EmployeeEntity entity) {
        return EmployeeDTO.builder()
                .empId(entity.getEmpId())
                .email(entity.getEmail())
                .userName(entity.getUserName())
                .mobileNumber(entity.getPhoneNumber())
                .description(entity.getDescription())
                .points(entity.getPoints())
                .role(entity.getRole())
                .build();
    }
}
