package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.adminPortal.DTO.ApprovalUpdateDTO;
import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin-portal")
public class ApprovalController {

    @Autowired
    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/nomination-status")
    public ResponseEntity<ResponseDTO> getApprovalStatus(@RequestParam String userId, @RequestParam int approvalLevel, @RequestParam int approvalStatus) throws Exception{
        return ResponseEntity.ok(ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                approvalService.getApprovalStatus(userId, approvalLevel, approvalStatus),
                null
        ));

    }

    @PutMapping("employee/change-status")
    public ResponseEntity<ResponseDTO> statusChange(@RequestBody List<ApprovalUpdateDTO> dto, @RequestParam String userId) throws Exception{

        return ResponseEntity.ok(ResponseDTO.build(
                true,
                "status changed successfully",
                LocalDateTime.now(),
                approvalService.updateApprovalStatusBulk(dto, userId),
                null
        ));

    }

    @GetMapping("/employee/approval-status")
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
