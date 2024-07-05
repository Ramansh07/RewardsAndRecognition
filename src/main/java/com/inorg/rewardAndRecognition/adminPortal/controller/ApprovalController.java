package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.adminPortal.DTO.ApprovalUpdateDTO;
import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.userPortal.dto.NominatorHistoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin-portal")
public class ApprovalController {

    @Autowired
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/status-approvals/{userId}/{approvalLevel}/{approvalStatus}")
    public ResponseEntity<ResponseDTO> getApprovalStatus(@PathVariable String userId, @PathVariable int approvalLevel, @PathVariable int approvalStatus) throws Exception{
        List<PendingApprovalsDTO> pendingApprovals = approvalService.getApprovalStatus(userId, approvalLevel, approvalStatus);

        ResponseDTO response = ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                pendingApprovals,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status-change/{userId}")
    public ResponseEntity<ResponseDTO> statusChange(@RequestBody List<ApprovalUpdateDTO> dto, @PathVariable String userId) throws Exception{


        ResponseDTO response = ResponseDTO.build(
                true,
                "status changed successfully",
                LocalDateTime.now(),
                approvalService.updateApprovalStatusBulk(dto, userId),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/approval-status")
    public ResponseEntity<ResponseDTO> getNominatorHistory(@RequestParam String nominatorId) throws Exception{

        return ResponseEntity.ok().body(ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                approvalService.nominatorHistory(nominatorId),
                null
        ));

    }

}
