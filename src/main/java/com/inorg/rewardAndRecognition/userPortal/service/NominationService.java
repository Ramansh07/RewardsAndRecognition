package com.inorg.rewardAndRecognition.userPortal.service;

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

@Service
public class NominationService {

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Transactional
    public List<ApprovalEntity> giveReward(NominateDTO nominateDTO) {
        List<ApprovalEntity> savedApprovals = new ArrayList<>();
        NominatorDTO nominator = nominateDTO.getNominator();
        List<NominationRewardDTO> rewards = nominateDTO.getRewards();
        List<NominateDTO> createdEntities = new ArrayList<>();

        NominationEntity nomination = new NominationEntity();
        nomination.setNominatorId(nominator.getId());

        for (NominationRewardDTO reward : rewards) {
            nomination.setRewardId(reward.getId());
            nomination.setJustification(reward.getTeamJustification());

            for(NomineeDTO nominee: reward.getNominees() ) {
                nomination.setNomineeId(nominee.getId());
                if(!nominee.getInlineJustification().isEmpty())nomination.setJustification(nominee.getInlineJustification());
                nomination.setCreatedDateTime(LocalDateTime.now());
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
