package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin-portal")
public class ApprovalController {
    @GetMapping("/l1-pending")
    public ResponseEntity<List<RewardDTO>> getLevelOnePendingApprovals() throws Exception {
        Optional<List<RewardDTO>> rewardDTOs = rewardService.getAllRewards();
        if (rewardDTOs.isPresent()) {
            return new ResponseEntity<>(rewardDTOs.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No rewards found");
        }
    }
}
