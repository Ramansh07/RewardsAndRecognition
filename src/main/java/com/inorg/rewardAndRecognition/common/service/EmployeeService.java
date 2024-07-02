package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeHistoryDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeRoleMappingEntity;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<EmployeeDTO> findAllActiveEmployees() throws ResourceNotFoundException;
    EmployeeDTO findActiveEmployeeById(String id) throws ResourceNotFoundException, InvalidRequest;
    EmployeeDTO findActiveEmployeeByEmail(String email) throws ResourceNotFoundException, InvalidRequest;
    List<EmployeeDTO> putRoles(List<SetRoleDTO> roleDTOs) throws ResourceNotFoundException, InvalidRequest;
    EmployeeDTO postDescription(String id, SetDescriptionDTO descriptionDTO) throws ResourceNotFoundException, InvalidRequest;
    List<EmployeeRoleMappingEntity> getEmployeeRoleMapping() throws  Exception;

}

