package com.inorg.rewardAndRecognition.adminPortal.service;
import com.inorg.rewardAndRecognition.adminPortal.DTO.ApprovalUpdateDTO;
import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import com.inorg.rewardAndRecognition.common.entity.HistoryEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.ApprovalRepository;
import com.inorg.rewardAndRecognition.common.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.common.repository.HistoryRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.common.service.RewardService;
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
    private HistoryRepository historyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;

    public List<PendingApprovalsDTO> getApprovalStatus(String userId, int approvalLevel, int approvalStatus)
            throws NoAuthorisationException, ResourceNotFoundException, InvalidRequest {
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
        List<PendingApprovalsDTO> pendingApprovalsDTOList = new ArrayList<>();
        for (ApprovalEntity approval : approvals) {

                System.out.println("approvalId "+ approval.getApprovalId()+"\n");
                NominationEntity nomination = null;
                for (NominationEntity tempNomination : temp) {
                    if (tempNomination.getNominationId() == approval.getNominationId()) {
                        nomination = tempNomination;
                        EmployeeDTO nominator = employeeService.findActiveEmployeeById(nomination.getNominatorId());
                        EmployeeDTO nominee = employeeService.findActiveEmployeeById(nomination.getNomineeId());
                        RewardDTO reward = rewardService.getRewardById(nomination.getRewardId());

                        PendingApprovalsDTO dto = PendingApprovalsDTO.builder()
                                .approvalId(approval.getApprovalId())
                                .nominatorName(nominator.getUserName())
                                .nomineeName(nominee.getUserName())
                                .justification(nomination.getJustification())
                                .rewardName(reward.getRewardName())
                                .build();

                        pendingApprovalsDTOList.add(dto);

                    }
                }
        }
        return pendingApprovalsDTOList;
    }

    @Transactional
    public void updateApprovalStatusBulk(List<ApprovalUpdateDTO> approvalUpdateDTOs, String userId)
            throws NoAuthorisationException, ResourceNotFoundException, InvalidRequest {
        EmployeeDTO user = employeeService.findActiveEmployeeById(userId);
        if (user.getRole() < rewardCreationAuthorityLevel) {
            throw new NoAuthorisationException("You are not authorized to update approval status");
        }

        for (ApprovalUpdateDTO dto : approvalUpdateDTOs) {
            ApprovalEntity approval = approvalRepository.findById(dto.getApprovalId())
                    .orElseThrow(() -> new ResourceNotFoundException("Approval not found: " + dto.getApprovalId()));
            validateStatusChange(approval, dto.getStatus());
            approval.setApprovalStatus(dto.getStatus());
            approvalRepository.save(approval);
            handleApprovalStatusChange(approval, dto.getStatus(), userId);
        }
    }

    private void validateStatusChange(ApprovalEntity approval, int newStatus) throws InvalidRequest {
        int currentStatus = approval.getApprovalStatus();

        if ((currentStatus == 1 && newStatus == -1) || (currentStatus == -1 && newStatus == 1)) {
            throw new InvalidRequest("Invalid status change request: Cannot change from " +
                    (currentStatus == 1 ? "approved to denied" : "denied to approved"));
        }
    }

    private void handleApprovalStatusChange(ApprovalEntity approval, int newStatus, String userId)
            throws ResourceNotFoundException {
        if (newStatus == -1) {
            nominationRepository.markAsDenied(approval.getNominationId());
        } else if (approval.getApprovalLevel() == 2 && newStatus == 1) {
            handleLevel2Approval(approval, userId);
        }
    }

    private void handleLevel2Approval(ApprovalEntity approval, String userId) {

        NominationEntity nomination = nominationRepository.findByNominationId(approval.getNominationId());

        HistoryEntity history = HistoryEntity.builder()
                .userId(nomination.getNomineeId())
                .rewardId(nomination.getRewardId())
                .nominationId(Math.toIntExact(nomination.getNominationId()))
                .rewardDate(LocalDateTime.now())
                .cdn("CDN Value") // Set appropriately
                .createdDateTime(LocalDateTime.now())
                .updatedDateTime(LocalDateTime.now())
                .createdBy(userId)
                .modifiedBy(userId)
                .build();

        historyRepository.save(history);
    }




}