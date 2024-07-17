package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.RewardLevelMappingEntity;
import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.config.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.RewardLevelMappingRepository;
import com.inorg.rewardAndRecognition.common.repository.RewardsRepository;
import com.inorg.rewardAndRecognition.config.exceptions.InvalidRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RewardLevelMappingRepository rewardLevelMappingRepository;
    public RewardServiceImpl(RewardsRepository rewardsRepository, EmployeeService employeeService) {
        this.rewardsRepository = rewardsRepository;
        this.employeeService = employeeService;
    }

    @Override
    @Transactional
    public List<RewardDTO> createRewards(String adminId, List<CreateRewardDTO> rewardDTOs) throws Exception {

        EmployeeDTO user = employeeService.findActiveEmployeeByEmail(adminId);
        if(user.getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not authorised to create a reward");
        }
        for (CreateRewardDTO createRewardDTO : rewardDTOs) {
            Optional<RewardsEntity>existingRewards = rewardsRepository.findByRewardNameAndIsActiveTrueAndIsDeletedFalse(createRewardDTO.getRewardName());
            if (existingRewards.isPresent()) {
                throw new InvalidRequest("Reward with the name " + createRewardDTO.getRewardName() + " already exists.");
            }
        }

        List<RewardsEntity> entitiesToSave = rewardDTOs.stream()
                .map(dto -> mapCreateDtoToEntity(dto, adminId))
                .collect(Collectors.toList());


        List<RewardsEntity> savedEntities = rewardsRepository.saveAll(entitiesToSave);

        return savedEntities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public List<RewardDTO> updateRewards(String adminId, List<RewardDTO> rewardDTOs) throws Exception {
        EmployeeDTO user = employeeService.findActiveEmployeeByEmail(adminId);
        if(user.getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not authorised to upadte a reward");
        }
        List<RewardsEntity> entitiesToUpdate = new ArrayList<>();
        for (RewardDTO dto : rewardDTOs) {
            Optional<RewardsEntity> optionalEntity = rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(dto.getRewardId());
            if (optionalEntity.isPresent()) {
                RewardsEntity temp = optionalEntity.get();
                temp.setRewardName(dto.getRewardName());
                temp.setPoints(dto.getPoints());
                temp.setLevel(dto.getRewardLevel());
                temp.setLastModifiedDateTime(LocalDateTime.now());
                temp.setLastModifiedBy(user.getEmpId());
                entitiesToUpdate.add(temp);
            }
            else{
                throw new ResourceNotFoundException("Such a reward doesn't exist in the database please verify");
            }
        }
        List<RewardsEntity> updatedEntities = rewardsRepository.saveAll(entitiesToUpdate);
        return updatedEntities.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RewardDTO> deleteRewards(List<Integer> rewardIds, String userId) throws InvalidRequest, Exception {
        EmployeeDTO user = employeeService.findActiveEmployeeByEmail(userId);
        if (user.getRole() < rewardCreationAuthorityLevel) {
            throw new NoAuthorisationException("You are not authorised to delete a reward");
        }

        List<Long> ids = rewardIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<RewardsEntity> existingRewards = new ArrayList<>();
        for (int id : rewardIds) {
            Optional<RewardsEntity> temp = rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(id);
            temp.ifPresent(existingRewards::add);
        }

        if (existingRewards.size() != ids.size()) {
            throw new InvalidRequest("One or more rewards do not exist.");
        }

        int updatedCount = rewardsRepository.softDeleteByIds(ids);
        if (updatedCount != ids.size()) {
            throw new Exception("Failed to delete the rewards");
        }

        List<RewardsEntity> deletedRewards = rewardsRepository.findAllById(ids);
        return deletedRewards.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public RewardDTO getRewardById(int id) throws ResourceNotFoundException {
        RewardsEntity entity = rewardsRepository.findByRewardIdAndIsActiveTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward not found with ID: " + id));
        return mapEntityToDto(entity);
    }

    @Override
    public List<RewardDTO> getAllRewards() throws ResourceNotFoundException {
        Optional<List<RewardsEntity>> entities = rewardsRepository.findByIsActiveTrueAndIsDeletedFalse();
        if (entities.isEmpty()) {
            throw new ResourceNotFoundException("No rewards found");
        }
        List<RewardsEntity>temp = entities.get();

        return temp.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<RewardLevelMappingEntity> gerRewardLevelMapping() throws Exception {
        return rewardLevelMappingRepository.findAll();
    }

    private RewardDTO mapEntityToDto(RewardsEntity entity) {
        return RewardDTO.builder()
                .rewardId(entity.getRewardId())
                .points(entity.getPoints())
                .rewardName(entity.getRewardName())
                .rewardLevel(entity.getLevel())
                .build();
    }

    private RewardsEntity mapCreateDtoToEntity(CreateRewardDTO dto, String id) {
        RewardsEntity entity = new RewardsEntity();
        entity.setPoints(dto.getPoints());
        entity.setRewardName(dto.getRewardName());
        entity.setLevel(dto.getRewardLevel());
        entity.setDeleted(false);
        entity.setActive(true);
        entity.setCreatedBy(id);
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setLastModifiedBy(id);
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
