package com.inorg.rewardAndRecognition.userPortal.controller;

import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.service.NominateService;
import com.inorg.rewardAndRecognition.userPortal.service.UserPortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user-portal")
public class NominateController {
    private final NominateService nominateService;

    @Autowired
    public NominateController(NominateService nominateService) {
        this.nominateService = nominateService;
    }

    @PostMapping("/nominate")
    public ResponseEntity<String> Nominate(@RequestBody NominateDTO nominateDTO) throws Exception{
        boolean success = nominateService.giveReward(nominateDTO);
        if(success)return new ResponseEntity<>("Nomination Successful", HttpStatus.CREATED);
        else return new ResponseEntity<>("Nomination not Successful ", HttpStatus.NOT_IMPLEMENTED);
    }
}
