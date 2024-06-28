package com.inorg.rewardAndRecognition.adminPortal.controller;

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
    public ResponseEntity<List<RewardDTO>> getRewards() throws Exception {
        Optional<List<RewardDTO>> rewardDTOs = rewardService.getAllRewards();
        if (rewardDTOs.isPresent()) {
            return new ResponseEntity<>(rewardDTOs.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("No rewards found");
        }
    }

    @PostMapping(path = "/rewards/create-rewards")
    public ResponseEntity<ResponseDTO> postRewards(@RequestBody List<RewardDTO> rewardDTOs) throws Exception {
        Optional<List<RewardDTO>> createdRewards = rewardService.createRewards(rewardDTOs);
        ResponseDTO responseDTO = ResponseDTO.build(true,
                "Rewards created successfully",
                LocalDateTime.now(),
                createdRewards,
                null);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}
