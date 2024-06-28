package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.adminPortal.DTO.PendingApprovalsDTO;
import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/pending-approvals/{approvalLevel}")
    public ResponseEntity<ResponseDTO> getPendingApprovals(@PathVariable int approvalLevel) {
        List<PendingApprovalsDTO> pendingApprovals = approvalService.getPendingApprovals(approvalLevel);

        ResponseDTO response = ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                pendingApprovals,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
