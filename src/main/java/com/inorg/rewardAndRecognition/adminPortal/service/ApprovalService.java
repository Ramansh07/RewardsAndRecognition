package com.inorg.rewardAndRecognition.adminPortal.service;

import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.repository.ApprovalRepository;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<PendingApprovalsDTO> getPendingApprovals(int approvalLevel) {
        List<ApprovalEntity> pendingApprovals = approvalRepository.findByApprovalLevelAndApprovalStatus(approvalLevel, 0); // 0 for pending status

        List<Integer> nominationIds = pendingApprovals.stream()
                .map(ApprovalEntity::getNominationId)
                .collect(Collectors.toList());

        List<NominationEntity> nominations = nominationRepository.findByNominationIdIn(nominationIds);

        return nominations.stream().map(nomination -> {
            EmployeeEntity nominator = employeeRepository.findActiveEmployeeById(nomination.getNominatorId());
            EmployeeEntity nominee = employeeRepository.findActiveEmployeeById(nomination.getNomineeId());

            return new PendingApprovalsDTO(
                    nominator != null ? nominator.getUserName() : null,
                    nominee != null ? nominee.getUserName() : null,
                    nomination.getJustification(),
                    "Reward Name" // Replace with actual reward name retrieval logic
            );
        }).collect(Collectors.toList());
    }
}