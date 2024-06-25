package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;

import java.util.List;

public interface CustomEmployeeRepository {

    List<EmployeeEntity> findActiveEmployees();
    EmployeeEntity findActiveEmployeeById(String id) throws Exception;
    EmployeeEntity findActiveEmployeeByEmail(String email) throws Exception;
    boolean updateDescription(String id, String description);
    //findActiveEmployeeByEmail
}
