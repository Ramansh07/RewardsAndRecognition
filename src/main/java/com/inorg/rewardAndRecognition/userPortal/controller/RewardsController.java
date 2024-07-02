package com.inorg.rewardAndRecognition.userPortal.controller;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


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
    @GetMapping("/rewardLevels")
    public ResponseEntity<ResponseDTO> getRewardLevels() throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Reward levels retrieved successfully",
                LocalDateTime.now(),
                rewardsService.gerRewardLevelMapping(),
                null));
    }
}
