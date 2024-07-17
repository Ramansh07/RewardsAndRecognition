package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.adminPortal.DTO.ApprovalUpdateDTO;
import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/nomination-status")
    public ResponseEntity<ResponseDTO> getApprovalStatus(HttpServletRequest request, @RequestParam int approvalLevel, @RequestParam int approvalStatus) throws Exception{
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                approvalService.getApprovalStatus(adminId, approvalLevel, approvalStatus),
                null
        ));

    }

    @PutMapping("employee/change-status")
    public ResponseEntity<ResponseDTO> statusChange(HttpServletRequest request,@RequestBody List<ApprovalUpdateDTO> dto) throws Exception{
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(ResponseDTO.build(
                true,
                "status changed successfully",
                LocalDateTime.now(),
                approvalService.updateApprovalStatusBulk(dto, adminId),
                null
        ));

    }

    @GetMapping("/employee/approval-status")
    public ResponseEntity<ResponseDTO> getNominatorHistory(HttpServletRequest request) throws Exception{
        String token = jwtTokenProvider.resolveToken(request);
        String nominatorId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok().body(ResponseDTO.build(
                true,
                "Pending approvals retrieved successfully",
                LocalDateTime.now(),
                approvalService.nominatorHistory(nominatorId),
                null
        ));

    }

}
