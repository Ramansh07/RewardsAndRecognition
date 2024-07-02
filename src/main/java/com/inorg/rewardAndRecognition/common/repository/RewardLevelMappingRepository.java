package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.RewardLevelMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardLevelMappingRepository extends JpaRepository<RewardLevelMappingEntity, Integer> {
}
