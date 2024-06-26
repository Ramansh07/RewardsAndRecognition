package com.inorg.rewardAndRecognition.userPortal.controller;
import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
import com.inorg.rewardAndRecognition.userPortal.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "/user-portal")
public class RewardsController {

    private final RewardsService rewardsService;

    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/rewards")
    public ResponseEntity<List<RewardDTO>> getRewards() throws Exception {
        List<RewardDTO> rewards = rewardsService.getAllRewards();
        return ResponseEntity.ok().body(rewards);
    }
    //getActiveRewardById
    @GetMapping("/reward/{rewardId}")
    public ResponseEntity<RewardDTO> getActiveRewardById(@PathVariable int rewardId) throws Exception {
        RewardDTO reward = rewardsService.getActiveRewardById(rewardId);
        return ResponseEntity.ok().body(reward);
    }


}
