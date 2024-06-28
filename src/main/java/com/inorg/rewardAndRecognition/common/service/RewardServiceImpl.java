package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.RewardsRepository;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {
    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;

    private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);
    private final RewardsRepository rewardsRepository;
    private final EmployeeService employeeService;
    public RewardServiceImpl(RewardsRepository rewardsRepository, EmployeeService employeeService) {
        this.rewardsRepository = rewardsRepository;
        this.employeeService = employeeService;
    }

    @Override
    @Transactional
    public Optional<List<RewardDTO>> createRewards(List<CreateRewardDTO> rewardDTOs) throws Exception {

        Optional<EmployeeDTO> user = employeeService.findActiveEmployeeById(rewardDTOs.getFirst().getUserId());
        if(user.isPresent()&& user.get().getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not authorised to create a reward");
        }

        for (CreateRewardDTO createRewardDTO : rewardDTOs) {
            List<RewardsEntity> existingRewards = rewardsRepository.findByRewardNameAndIsActiveTrueAndIsDeletedFalse(createRewardDTO.getRewardName());
            if (!existingRewards.isEmpty()) {
                throw new NoAuthorisationException("Reward with the name " + createRewardDTO.getRewardName() + " already exists.");
            }
        }

        List<RewardsEntity> entitiesToSave = rewardDTOs.stream()
                .map(this::mapCreateDtoToEntity)
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
        Optional<EmployeeDTO> user = employeeService.findActiveEmployeeById(rewardDTOs.getFirst().getUserId());
        if(user.isPresent()&& user.get().getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not authorised to upadte a reward");
        }
        List<RewardsEntity> entitiesToUpdate = new ArrayList<>();
        for (RewardDTO dto : rewardDTOs) {
            RewardsEntity entity = rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(dto.getRewardId());
            if (entity != null) {
                entity.setRewardName(dto.getRewardName());
                entity.setPoints(dto.getPoints());
                entity.setLevel(dto.getRewardLevel());
                entitiesToUpdate.add(entity);
            }
            else{
                throw new ResourceNotFoundException("Such a reward doesn't exist in the database please verify");
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
    public Optional<List<RewardDTO>> deleteRewards(List<Integer> rewardIds, String userId) throws InvalidRequest, Exception {
        Optional<EmployeeDTO> user = employeeService.findActiveEmployeeById(userId);
        if(user.isPresent()&& user.get().getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not authorised to delete a reward");
        }
        List<Long> ids = rewardIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<RewardsEntity> existingRewards = new ArrayList<>();
        for(int id : rewardIds){
           existingRewards.add(rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(id));
        }

        if (existingRewards.size() != ids.size()) {
            throw new InvalidRequest("One or more rewards do not exist.");
        }

        Optional<List<RewardsEntity>> deletedRewards =  rewardsRepository.softDeleteByIds(ids);
        if(deletedRewards.isEmpty()){
            throw new Exception("failed to delete the rewards");
        }
        List<RewardDTO> rewardDTOList = deletedRewards.get().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return Optional.of(rewardDTOList);

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

    private RewardsEntity mapCreateDtoToEntity(CreateRewardDTO dto) {
        RewardsEntity entity = new RewardsEntity();
        entity.setPoints(dto.getPoints());
        entity.setRewardName(dto.getRewardName());
        entity.setLevel(dto.getRewardLevel());
        entity.setDeleted(false);
        entity.setActive(true);
        entity.setCreatedBy(dto.getUserId());
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setLastModifiedBy(dto.getUserId());
        entity.setLastModifiedDateTime(LocalDateTime.now());
        return entity;
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
