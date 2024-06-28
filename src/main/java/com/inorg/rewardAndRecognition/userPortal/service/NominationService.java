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
        NominationEntity nomination = new NominationEntity();
        Optional<EmployeeDTO> nominatorCheck = employeeService.findActiveEmployeeById(nominatorId);
        nomination.setNominatorId(nominatorId);

        for (NominationRewardDTO reward : rewards) {
            int rewardId = reward.getId();
            Optional<RewardDTO> rewardCheck  = rewardService.getRewardById(rewardId);
            nomination.setRewardId(rewardId);
            nomination.setJustification(reward.getTeamJustification());

            if(rewardCheck.isPresent() && rewardCheck.get().getRewardLevel()>nominatorCheck.get().getRole()){
                throw new NoAuthorisationException("you are not authorised to give out the reward, Please talk to admin @inorg.com");
            }

            for(NomineeDTO nominee: reward.getNominees() ) {
                String nomineeId = nominee.getId();
                Optional<EmployeeDTO> nomineeCheck = employeeService.findActiveEmployeeById(nomineeId);
                nomination.setNomineeId(nomineeId);
                if(!nominee.getInlineJustification().isEmpty())nomination.setJustification(nominee.getInlineJustification());
                nomination.setCreatedDateTime(LocalDateTime.now());
                nomination.setLastModifiedDateTime(LocalDateTime.now());
                nomination.setIsActive(true);
                nomination.setIsDeleted(false);
                NominationEntity createdNomination = nominationRepository.save(nomination);
                ApprovalEntity approval = new ApprovalEntity();
                approval.setNominationId(Math.toIntExact(createdNomination.getNominationId()));
                approval.setApprovalLevel(1);
                approval.setApprovalStatus(0);
                ApprovalEntity savedApproval = approvalRepository.save(approval);
                savedApprovals.add(savedApproval);
            }

        }

        return savedApprovals;
    }
}
