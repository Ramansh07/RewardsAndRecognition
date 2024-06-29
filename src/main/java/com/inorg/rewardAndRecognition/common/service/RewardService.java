package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface RewardService {
    List<RewardDTO> createRewards(List<CreateRewardDTO> rewardDTOs) throws Exception;
    List<RewardDTO> updateRewards(List<RewardDTO> rewardDTOs) throws Exception;
    List<RewardDTO> deleteRewards(List<Integer> rewardIds, String userId) throws InvalidRequest, Exception;
    RewardDTO getRewardById(int id) throws ResourceNotFoundException;
    List<RewardDTO> getAllRewards() throws ResourceNotFoundException;
}

