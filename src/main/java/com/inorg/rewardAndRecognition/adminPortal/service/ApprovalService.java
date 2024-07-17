package com.inorg.rewardAndRecognition.adminPortal.service;
import com.inorg.rewardAndRecognition.adminPortal.DTO.ApprovalUpdateDTO;
import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeHistoryDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.service.NominationServiceCommon;
import com.inorg.rewardAndRecognition.config.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.config.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.repository.ApprovalRepository;
import com.inorg.rewardAndRecognition.common.repository.HistoryRepository;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import com.inorg.rewardAndRecognition.userPortal.dto.NominatorHistoryDTO;
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
    private NominationRepository nominationRepository;
    @Autowired
    private NominationServiceCommon nominationServiceCommon;
    @Autowired
    private ApprovalRepository approvalRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private HistoryRepository historyRepository;
    @Value("${reward.creation.authority}")
    private  int rewardCreationAuthorityLevel;
    @Value("${approval.min.authority}")
    private int minApprovalAuthority;
    @Value("${approval.min.level}")
    private int minApprovalLevel;
    @Value("${approval.max.level}")
    private int maxApprovalLevel;



    public List<PendingApprovalsDTO> getApprovalStatus(String userId, int approvalLevel, int approvalStatus)
            throws NoAuthorisationException, ResourceNotFoundException, InvalidRequest {

        EmployeeDTO user = employeeService.findActiveEmployeeByEmail(userId);
        if (user.getRole() < minApprovalAuthority + approvalLevel -1) {
            throw new NoAuthorisationException("You are not authorized to see the approval status of rewards of such level");
        }

        List<ApprovalEntity> approvals = approvalRepository.findByApprovalLevelAndApprovalStatus(approvalLevel, approvalStatus);
        List<Integer> nominationIds = approvals.stream()
                .map(ApprovalEntity::getNominationId)
                .collect(Collectors.toList());

        List<NominationEntity> temp = nominationServiceCommon.findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(nominationIds);

        List<PendingApprovalsDTO> pendingApprovalsDTOList = new ArrayList<>();
        for (ApprovalEntity approval : approvals) {
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
                                .justificationForNomination(nomination.getJustification())
                                .justificationForApprovalDenial(approval.getJustification())
                                .rewardName(reward.getRewardName())
                                .dateTime(tempNomination.getCreatedDateTime())
                                .build();
                        pendingApprovalsDTOList.add(dto);

                    }
                }
        }
        return pendingApprovalsDTOList;
    }

    public ApprovalEntity findApprovalById(Long id) throws Exception {
        Optional<ApprovalEntity> resp = approvalRepository.findById(id);
        if(resp.isEmpty())throw new ResourceNotFoundException("the approval id which you are trying to access doesn't exist. ID: "+ id);
        return resp.get();
    }

    @Transactional
    public String updateApprovalStatusBulk(List<ApprovalUpdateDTO> approvalUpdateDTOs, String userId) throws Exception {
        EmployeeDTO user = employeeService.findActiveEmployeeByEmail(userId);

        for (ApprovalUpdateDTO dto : approvalUpdateDTOs) {
            ApprovalEntity approval = findApprovalById(dto.getApprovalId());
            validateStatusChange(approval, dto.getStatus(), user.getRole());
            approval.setApprovalStatus(dto.getStatus());
            approval.setJustification(dto.getJustification());
            approval.setLastModifiedBy(userId);
            approval.setLastModifiedAt(LocalDateTime.now());
            approvalRepository.save(approval);
            if (approval.getApprovalLevel() < maxApprovalLevel && dto.getStatus() == 1) {
                ApprovalEntity approvalLevel2 = new ApprovalEntity();
                approvalLevel2.setNominationId(approval.getNominationId());
                approvalLevel2.setApprovalLevel(approval.getApprovalLevel()+1);
                approvalLevel2.setApprovalStatus(0);
                approvalRepository.save(approvalLevel2);
            }
            handleApprovalStatusChange(approval, dto.getStatus(), userId);
        }
        return "success";

    }

    private void validateStatusChange(ApprovalEntity approval, int newStatus, int userRole) throws Exception {
        int currentStatus = approval.getApprovalStatus();
        int currentLevel = approval.getApprovalLevel();
        if(userRole<minApprovalAuthority+currentLevel-1)throw new NoAuthorisationException("you are not authorised to change status of this reward");
        if ((currentStatus == 1 && newStatus == -1) || (currentStatus == -1 && newStatus == 1)) {
            throw new InvalidRequest("Invalid status change request: Cannot change from " +
                    (currentStatus == 1 ? "approved to denied" : "denied to approved"));
        }

    }

    private void handleApprovalStatusChange(ApprovalEntity approval, int newStatus, String userId)
            throws ResourceNotFoundException {
        int nominationId = approval.getNominationId();
        if (newStatus == -1) {
            nominationRepository.markAsDenied(nominationId);
        } else if (approval.getApprovalLevel() == maxApprovalLevel && newStatus == 1) {
            nominationRepository.markAsApproved(nominationId);
        }
        else{
            nominationRepository.incrementLevel(nominationId);
        }
    }

    public List<EmployeeHistoryDTO> findEmployeeHistory(String adminId) throws Exception {
        EmployeeDTO employeeObject = employeeService.findActiveEmployeeByEmail(adminId);
        String id = employeeObject.getEmpId();
        List<EmployeeHistoryDTO>resp = new ArrayList<>();
        List<NominationEntity> nominationEntities = nominationServiceCommon.findByNomineeIdAndStatus(id, 1);

            for(NominationEntity nominationEntity:nominationEntities ){
                String nominator = nominationEntity.getNominatorId();
                String nominatorName = employeeService.findActiveEmployeeById(nominator).getUserName();
                int rewardId = nominationEntity.getRewardId();
                String rewardName = rewardService.getRewardById(rewardId).getRewardName();
                String justification = nominationEntity.getJustification();
                resp.add(
                        EmployeeHistoryDTO.builder()
                                .nominatorName(nominatorName)
                                .nominationJustification(justification)
                                .rewardName(rewardName)
                                .dateTime(nominationEntity.getLastModifiedDateTime())
                                .build()
                );
            }

        return resp;
    }
    public String convertIntegerStatusToString(int a){
        if(a==1)return "approved";
        else if(a==0)return "pending";
        else return "denied";
    }

    public List<NominatorHistoryDTO> nominatorHistory(String nominatorEmailId) throws  Exception{
        EmployeeDTO employeeObject = employeeService.findActiveEmployeeByEmail(nominatorEmailId);
        String nominatorId = employeeObject.getEmpId();
       List<NominationEntity> nominationEntities = nominationServiceCommon.findByNominatorId(nominatorId);

        List<NominatorHistoryDTO> resp = new ArrayList<>();
        for (NominationEntity nomination : nominationEntities){
            String reasonForDenial = "";
            if (nomination.getStatus()==-1){
                Optional<String>denialReason = approvalRepository.findJustificationByNominationIdAndApprovalStatus(nomination.getNominationId(),-1);
                reasonForDenial = denialReason.orElse("");
            }
            resp.add(
                    NominatorHistoryDTO.builder()
                            .currentLevel(nomination.getLevel())
                            .currentStatus(convertIntegerStatusToString(nomination.getStatus()))
                            .justificationForNomination(nomination.getJustification())
                            .nomineeName(employeeService.findActiveEmployeeById(nomination.getNomineeId()).getUserName())
                            .reasonForDenial(reasonForDenial)
                            .dateTime(nomination.getCreatedDateTime())
                            .build()
            );
        }
        return resp;
    }
}