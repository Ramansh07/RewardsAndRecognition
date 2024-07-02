package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.EmployeeRoleMappingEntity;
import com.inorg.rewardAndRecognition.common.entity.RewardLevelMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleMappingRepository extends JpaRepository<EmployeeRoleMappingEntity, Integer> {

}
