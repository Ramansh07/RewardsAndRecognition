package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<RewardsEntity, Long>, CustomRewardRepository{
}
