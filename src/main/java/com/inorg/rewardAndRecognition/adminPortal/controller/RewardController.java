package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.adminPortal.DTO.CreateRewardDTO;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import com.inorg.rewardAndRecognition.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("admin-portal")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

    @GetMapping("/rewardLevels")
    public ResponseEntity<ResponseDTO> getRewardLevels() throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Reward levels retrieved successfully",
                LocalDateTime.now(),
                rewardService.gerRewardLevelMapping(),
                null));
    }

    @PostMapping(path = "rewards/create-rewards")
    public ResponseEntity<ResponseDTO> postRewards(@RequestBody List<CreateRewardDTO> rewardDTOs, HttpServletRequest request) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.build(true,
                "Rewards created successfully",
                LocalDateTime.now(),
                rewardService.createRewards(adminId, rewardDTOs),
                null));
    }
    @PutMapping(path = "rewards/update-rewards")
    public ResponseEntity<ResponseDTO> updateRewards(@RequestBody List<RewardDTO> rewardDTOs, HttpServletRequest request) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards updated successfully",
                LocalDateTime.now(),
                rewardService.updateRewards(adminId, rewardDTOs),
                null));
    }
    @PutMapping(path = "rewards/delete-rewards")
    public ResponseEntity<ResponseDTO> deleteRewards(@RequestBody List<Integer> rewardIds, HttpServletRequest request) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Rewards deleted successfully",
                LocalDateTime.now(),
                rewardService.deleteRewards(rewardIds, adminId),
                null));
    }
}

