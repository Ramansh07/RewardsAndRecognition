package com.inorg.rewardAndRecognition.userPortal.controller;

import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user-portal")
public class RewardsController {

    private final RewardService rewardsService;

    @Autowired
    public RewardsController(RewardService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/rewards")
    public ResponseEntity<ResponseDTO> getRewards() throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards retrieved successfully",
                LocalDateTime.now(),
                rewardsService.getAllRewards(),
                null));
    }

    @GetMapping("/reward/{rewardId}")
    public ResponseEntity<ResponseDTO> getActiveRewardById(@PathVariable int rewardId) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards retrieved successfully",
                LocalDateTime.now(),
                rewardsService.getRewardById(rewardId),
                null));
    }

}
