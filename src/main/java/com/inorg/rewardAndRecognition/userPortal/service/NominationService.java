package com.inorg.rewardAndRecognition.userPortal.service;

import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominatorDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominationRewardDTO;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.repository.ApprovalRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import com.inorg.rewardAndRecognition.userPortal.dto.NomineeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NominationService {

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RewardService rewardService;

    @Transactional
    public List<ApprovalEntity> giveReward(NominateDTO nominateDTO) throws Exception {
        List<ApprovalEntity> savedApprovals = new ArrayList<>();
        NominatorDTO nominator = nominateDTO.getNominator();
        List<NominationRewardDTO> rewards = nominateDTO.getRewards();
        String nominatorId  = nominator.getId();

        EmployeeDTO nominatorCheck = employeeService.findActiveEmployeeById(nominatorId);


        for (NominationRewardDTO reward : rewards) {
            int rewardId = reward.getId();
            RewardDTO rewardCheck  = rewardService.getRewardById(rewardId);

            if(rewardCheck.getRewardLevel()>nominatorCheck.getRole()){
                throw new NoAuthorisationException("you are not authorised to give out the reward, Please talk to admin @inorg.com");
            }

            for(NomineeDTO nominee: reward.getNominees() ) {
                String nomineeId = nominee.getId();
                EmployeeDTO nomineeCheck = employeeService.findActiveEmployeeById(nomineeId);
                NominationEntity createdNomination =nominationRepository.save(NominationEntity.builder()
                        .nominatorId(nominatorId)
                        .nomineeId(nomineeId)
                        .rewardId(rewardId)
                        .justification(!nominee.getInlineJustification().isEmpty()?nominee.getInlineJustification(): reward.getTeamJustification())
                        .createdDateTime(LocalDateTime.now())
                        .lastModifiedDateTime(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .level(1)
                        .status(0)
                        .build());

                ApprovalEntity savedApproval = approvalRepository.save(ApprovalEntity.builder()
                          .nominationId(Math.toIntExact(createdNomination.getNominationId()))
                          .approvalLevel(1)
                          .approvalStatus(0)
                          .createdAt(LocalDateTime.now())
                          .lastModifiedAt(LocalDateTime.now())
                          .createdBy(nominatorId)
                          .lastModifiedBy(nominatorId)
                          .build());

                savedApprovals.add(savedApproval);
            }

        }

        return savedApprovals;
    }
}
