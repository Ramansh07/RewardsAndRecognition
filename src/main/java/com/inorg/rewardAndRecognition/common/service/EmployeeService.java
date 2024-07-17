package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeRoleMappingEntity;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.config.exceptions.InvalidRequest;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> findAllActiveEmployees() throws ResourceNotFoundException;
    EmployeeDTO findActiveEmployeeById(String id) throws ResourceNotFoundException, InvalidRequest;
    EmployeeDTO findActiveEmployeeByEmail(String email) throws ResourceNotFoundException, InvalidRequest;
    List<EmployeeDTO> putRoles(String adminId, List<SetRoleDTO> roleDTOs) throws Exception;
    EmployeeDTO postDescription(String id, SetDescriptionDTO descriptionDTO) throws Exception;
    List<EmployeeRoleMappingEntity> getEmployeeRoleMapping() throws  Exception;

}

