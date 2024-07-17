package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.RewardLevelMappingEntity;
import com.inorg.rewardAndRecognition.config.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import java.util.List;

public interface RewardService {
    List<RewardDTO> createRewards(String adminId, List<CreateRewardDTO> rewardDTOs) throws Exception;
    List<RewardDTO> updateRewards(String adminId,List<RewardDTO> rewardDTOs) throws Exception;
    List<RewardDTO> deleteRewards(List<Integer> rewardIds, String userId) throws InvalidRequest, Exception;
    RewardDTO getRewardById(int id) throws ResourceNotFoundException;
    List<RewardDTO> getAllRewards() throws ResourceNotFoundException;
    List<RewardLevelMappingEntity> gerRewardLevelMapping() throws Exception;
}

