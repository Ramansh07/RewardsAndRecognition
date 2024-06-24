package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;

import java.util.List;

public interface CustomRewardRepository {
    List<RewardsEntity> findAllActiveRewards();
    RewardsEntity findActiveRewardById(int rewardId);
}
