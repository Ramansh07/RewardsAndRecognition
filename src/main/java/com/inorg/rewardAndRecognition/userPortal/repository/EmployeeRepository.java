package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, CustomEmployeeRepository {
}
