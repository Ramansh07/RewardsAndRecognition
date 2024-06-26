package com.inorg.rewardAndRecognition.userPortal.service;
import com.inorg.rewardAndRecognition.userPortal.dto.EmployeeDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.userPortal.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.userPortal.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPortalService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public UserPortalService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> FindAllActiveEmployees() throws Exception{

        List<EmployeeEntity> listEmployeeEntity = employeeRepository.findActiveEmployees();
        if(!listEmployeeEntity.isEmpty()){
            ArrayList<EmployeeDTO> resp =  new ArrayList<>();
            for (EmployeeEntity employeeEntity : listEmployeeEntity) {
                EmployeeDTO employeeDTO = EmployeeDTO.builder()
                        .empId(employeeEntity.getEmpId())
                        .email(employeeEntity.getEmail())
                        .userName(employeeEntity.getUserName())
                        .mobileNumber(employeeEntity.getPhoneNumber())
                        .description(employeeEntity.getDescription())
                        .points(employeeEntity.getPoints())
                        .role(employeeEntity.getRole())
                        .build();

                resp.add(employeeDTO);
            }
            return resp;
        }
        else{
            throw new ResourceNotFoundException("There are no employees in the database right now");
        }
    }
    public EmployeeDTO FindActiveEmployeeById(String id) throws Exception{

        if(id == null || id.isEmpty()){
            throw new InvalidRequest("Id of employee cannot be null or blank");
        }
       EmployeeEntity employeeEntity = employeeRepository.findActiveEmployeeById(id);
       if(employeeEntity!=null) {

           EmployeeDTO employeeDTO = EmployeeDTO.builder()
                   .empId(employeeEntity.getEmpId())
                   .email(employeeEntity.getEmail())
                   .userName(employeeEntity.getUserName())
                   .mobileNumber(employeeEntity.getPhoneNumber())
                   .description(employeeEntity.getDescription())
                   .points(employeeEntity.getPoints())
                   .role(employeeEntity.getRole())
                   .build();
           return employeeDTO;


       }
       else{
           throw new ResourceNotFoundException("No Employee is present with the id" + id);
       }

    }
    public EmployeeDTO FindActiveEmployeeByEmail(String email) throws Exception{


        if(email == null || email.isEmpty()){
            throw new InvalidRequest("email of employee cannot be null or blank");
        }
        EmployeeEntity employeeEntity = employeeRepository.findActiveEmployeeByEmail(email);
        if(employeeEntity!=null) {

            EmployeeDTO employeeDTO = EmployeeDTO.builder()
                    .empId(employeeEntity.getEmpId())
                    .email(employeeEntity.getEmail())
                    .userName(employeeEntity.getUserName())
                    .mobileNumber(employeeEntity.getPhoneNumber())
                    .description(employeeEntity.getDescription())
                    .points(employeeEntity.getPoints())
                    .role(employeeEntity.getRole())
                    .build();
            return employeeDTO;


        }
        else{
            throw new ResourceNotFoundException("No Employee is present with the id" + email);
        }

    }
    public boolean postDescription(String id, SetDescriptionDTO descriptionDTO) throws Exception{
        if(id == null || id.isEmpty()){
            throw new InvalidRequest("Id of employee cannot be null or blank");
        }
        EmployeeDTO employeeDTO = FindActiveEmployeeById(id);
        return employeeRepository.updateDescription(id, descriptionDTO.getDescription());

    }





}
