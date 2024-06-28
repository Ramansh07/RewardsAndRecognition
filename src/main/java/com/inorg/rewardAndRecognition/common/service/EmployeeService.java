package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<List<EmployeeDTO>> findAllActiveEmployees() throws ResourceNotFoundException;
    Optional<EmployeeDTO> findActiveEmployeeById(String id) throws ResourceNotFoundException, InvalidRequest;
    Optional<EmployeeDTO> findActiveEmployeeByEmail(String email) throws ResourceNotFoundException, InvalidRequest;
    Optional<List<EmployeeDTO>> putRoles(List<SetRoleDTO> roleDTOs) throws ResourceNotFoundException, InvalidRequest;
    Optional<EmployeeDTO> postDescription(String id, SetDescriptionDTO descriptionDTO) throws ResourceNotFoundException, InvalidRequest;
}

