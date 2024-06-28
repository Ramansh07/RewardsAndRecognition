package com.inorg.rewardAndRecognition.userPortal.controller;

import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.service.NominateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/user-portal")
public class NominateController {
    private final NominateService nominateService;

    @Autowired
    public NominateController(NominateService nominateService) {
        this.nominateService = nominateService;
    }

    @PostMapping("/nominate")
    public ResponseEntity<ResponseDTO> nominate(@RequestBody NominateDTO nominateDTO) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee nominated successfully",
                LocalDateTime.now(),
                nominateService.giveReward(nominateDTO),
                null));
    }
}
