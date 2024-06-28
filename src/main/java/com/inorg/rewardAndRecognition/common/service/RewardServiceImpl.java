package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.RewardsRepository;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {
    private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);
    private final RewardsRepository rewardsRepository;
    public RewardServiceImpl(RewardsRepository rewardsRepository) {
        this.rewardsRepository = rewardsRepository;
    }

    @Override
    @Transactional
    public Optional<List<RewardDTO>> createRewards(List<RewardDTO> rewardDTOs) throws Exception {
        List<RewardsEntity> entitiesToSave = rewardDTOs.stream()
                .map(this::mapDtoToEntity)
                .collect(Collectors.toList());

        List<RewardsEntity> savedEntities = rewardsRepository.saveAll(entitiesToSave);
        List<RewardDTO> rewardDTOList = savedEntities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return Optional.of(rewardDTOList);
    }

    @Override
    @Transactional
    public Optional<List<RewardDTO>> updateRewards(List<RewardDTO> rewardDTOs) throws Exception {
        List<RewardsEntity> entitiesToUpdate = new ArrayList<>();
        for (RewardDTO dto : rewardDTOs) {
            RewardsEntity entity = rewardsRepository.findById((long) dto.getRewardId()).orElse(null);
            if (entity != null) {
                entity.setRewardName(dto.getRewardName());
                entity.setPoints(dto.getPoints());
                entity.setLevel(dto.getRewardLevel());
                entitiesToUpdate.add(entity);
            }
        }
        List<RewardsEntity> updatedEntities = rewardsRepository.saveAll(entitiesToUpdate);
        List<RewardDTO> rewardDTOList = updatedEntities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return Optional.of(rewardDTOList);
    }

    @Override
    @Transactional
    public void deleteRewards(List<Integer> rewardIds) throws InvalidRequest, Exception {
        List<Long> ids = rewardIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<RewardsEntity> existingRewards = rewardsRepository.findByIdIn(ids);
        if (existingRewards.size() != ids.size()) {
            throw new InvalidRequest("One or more rewards do not exist.");
        }

        rewardsRepository.softDeleteByIds(ids);
    }

    @Override
    public Optional<RewardDTO> getRewardById(int id) throws ResourceNotFoundException {
        RewardsEntity entity = rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(id);
        if (entity != null) {
            return Optional.of(mapEntityToDto(entity));
        } else {
            throw new ResourceNotFoundException("Reward not found with ID: " + id);
        }
    }

    @Override
    public Optional<List<RewardDTO>> getAllRewards() throws ResourceNotFoundException {
        List<RewardsEntity> entities = rewardsRepository.findByIsActiveTrueAndIsDeletedFalse();
        if (entities.isEmpty()) {
            throw new ResourceNotFoundException("No rewards found");
        }
        List<RewardDTO> rewardDTOList = entities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return Optional.of(rewardDTOList);
    }

    private RewardDTO mapEntityToDto(RewardsEntity entity) {
        return RewardDTO.builder()
                .rewardId(entity.getRewardId())
                .points(entity.getPoints())
                .rewardName(entity.getRewardName())
                .rewardLevel(entity.getLevel())
                .build();
    }

    private RewardsEntity mapDtoToEntity(RewardDTO dto) {
        RewardsEntity entity = new RewardsEntity();
        entity.setRewardId(dto.getRewardId());
        entity.setPoints(dto.getPoints());
        entity.setRewardName(dto.getRewardName());
        entity.setLevel(dto.getRewardLevel());
        entity.setDeleted(false);
        entity.setActive(true);
        return entity;
    }
}
