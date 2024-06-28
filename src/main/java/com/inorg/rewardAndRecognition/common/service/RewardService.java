package com.inorg.rewardAndRecognition.common.service;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface RewardService {
    Optional<List<RewardDTO>> createRewards(List<RewardDTO> rewardDTOs) throws Exception;
    Optional<List<RewardDTO>> updateRewards(List<RewardDTO> rewardDTOs) throws Exception;
    void deleteRewards(List<Integer> rewardIds) throws InvalidRequest, Exception;
    Optional<RewardDTO> getRewardById(int id) throws ResourceNotFoundException;
    Optional<List<RewardDTO>> getAllRewards() throws ResourceNotFoundException;
}

