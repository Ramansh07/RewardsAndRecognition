package com.inorg.rewardAndRecognition.adminPortal.controller;

import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin-portal")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/rewards")
    public ResponseEntity<ResponseDTO> getRewards() throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards retrieved successfully",
                LocalDateTime.now(),
                rewardService.getAllRewards(),
                null));
    }

    @PostMapping(path = "/rewards/create-rewards")
    public ResponseEntity<ResponseDTO> postRewards(@RequestBody List<CreateRewardDTO> rewardDTOs) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards created successfully",
                LocalDateTime.now(),
                rewardService.createRewards(rewardDTOs),
                null));
    }
    @PutMapping(path = "/rewards/update-rewards")
    public ResponseEntity<ResponseDTO> updateRewards(@RequestBody List<RewardDTO> rewardDTOs) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards updated successfully",
                LocalDateTime.now(),
                rewardService.updateRewards(rewardDTOs),
                null));
    }
    @PutMapping(path = "/rewards/delete-rewards/{userId}")
    public ResponseEntity<ResponseDTO> deleteRewards(@PathVariable String userId, @RequestBody List<Integer> rewardIds) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards updated successfully",
                LocalDateTime.now(),
                rewardService.deleteRewards(rewardIds, userId),
                null));
    }
}

