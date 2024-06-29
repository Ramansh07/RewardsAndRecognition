package com.inorg.rewardAndRecognition.adminPortal.service;

import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
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
import com.inorg.rewardAndRecognition.common.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
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
    private RewardService rewardService;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;

    public List<PendingApprovalsDTO> getApprovalStatus(String userId, int approvalLevel, int approvalStatus) throws NoAuthorisationException, ResourceNotFoundException, InvalidRequest {
        EmployeeDTO user = employeeService.findActiveEmployeeById(userId);
        if (user.getRole() < rewardCreationAuthorityLevel) {
            throw new NoAuthorisationException("You are not authorized to see the approval status of rewards");
        }

        List<ApprovalEntity> approvals = approvalRepository.findByApprovalLevelAndApprovalStatus(approvalLevel, approvalStatus);
        List<Integer> nominationIds = approvals.stream()
                .map(ApprovalEntity::getNominationId)
                .collect(Collectors.toList());

        Optional<List<NominationEntity>> nominations = nominationRepository.findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(nominationIds);
        if (nominations.isEmpty()) {
            throw new ResourceNotFoundException("No nominations found");
        }
        List<NominationEntity> temp = nominations.get();
        return temp.stream().map(nomination -> {
            try {
                EmployeeDTO nominator = employeeService.findActiveEmployeeById(nomination.getNominatorId());
                EmployeeDTO nominee = employeeService.findActiveEmployeeById(nomination.getNomineeId());
                RewardDTO reward = rewardService.getRewardById(nomination.getRewardId());

                return new PendingApprovalsDTO(
                        nominator.getUserName(),
                        nominee.getUserName(),
                        nomination.getJustification(),
                        reward.getRewardName()
                );
            } catch (Exception e) {
                throw new RuntimeException("Error processing nomination: " + nomination.getNominationId(), e);
            }
        }).collect(Collectors.toList());
    }


}