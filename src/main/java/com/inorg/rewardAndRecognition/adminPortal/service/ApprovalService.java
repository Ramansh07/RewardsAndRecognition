package com.inorg.rewardAndRecognition.adminPortal.service;

import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.ApprovalRepository;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;

    public List<PendingApprovalsDTO> getApprovalStatus(String userId, int approvalLevel, int approvalStatus) throws NoAuthorisationException, InvalidRequest, ResourceNotFoundException {
        Optional<EmployeeDTO> user = employeeService.findActiveEmployeeById(userId);
        if(user.isPresent()&& user.get().getRole() < rewardCreationAuthorityLevel){
            throw new NoAuthorisationException("You are not see approval status of rewards");
        }
        List<ApprovalEntity> approvals = approvalRepository.findByApprovalLevelAndApprovalStatus(approvalLevel, approvalStatus);
        List<Integer> nominationIds = approvals.stream()
                .map(ApprovalEntity::getNominationId)
                .collect(Collectors.toList());

        List<NominationEntity> nominations = nominationRepository.findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(nominationIds);

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