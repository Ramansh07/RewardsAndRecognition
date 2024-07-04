package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeHistoryDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.entity.EmployeeRoleMappingEntity;
import com.inorg.rewardAndRecognition.common.entity.HistoryEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRoleMappingRepository;
import com.inorg.rewardAndRecognition.common.repository.HistoryRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public List<EmployeeDTO> putRoles(List<SetRoleDTO> roleDTOs) throws ResourceNotFoundException, InvalidRequest {
        List<String> employeeIds = roleDTOs.stream()
                .map(SetRoleDTO::getEmployeeId)
                .collect(Collectors.toList());

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
    public EmployeeDTO postDescription(String id, SetDescriptionDTO descriptionDTO) throws ResourceNotFoundException, InvalidRequest {
        if (id == null || id.isEmpty()) {
            throw new InvalidRequest("Employee ID cannot be null or empty");
        }
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
