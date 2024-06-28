package com.inorg.rewardAndRecognition.userPortal.service;
import com.inorg.rewardAndRecognition.common.DTO.EmployeeDTO;
import com.inorg.rewardAndRecognition.common.DTO.RewardDTO;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.common.service.RewardService;
import com.inorg.rewardAndRecognition.userPortal.Utility.NominationHelper;
import com.inorg.rewardAndRecognition.userPortal.dto.*;
import com.inorg.rewardAndRecognition.common.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.userPortal.repository.NominateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NominateService {
    private final NominateRepository nominateRepository;
    private final RewardService rewardService;
    private final EmployeeService employeeService;

    @Autowired
    public NominateService(EmployeeService employeeService, RewardService rewardService, NominateRepository nominateRepository) {
        this.nominateRepository = nominateRepository;
        this.rewardService = rewardService;
        this.employeeService = employeeService;

    }
    public Boolean giveReward(NominateDTO nominateDTO) throws Exception {
        String nominatorId = nominateDTO.getNominator().getId();
        Optional<EmployeeDTO> nominatorCheck = employeeService.findActiveEmployeeById(nominatorId);
        if(nominatorCheck.isEmpty())throw new ResourceNotFoundException("No such nominator found");
        List<NominationHelper> nominations = new ArrayList<>();
        for(NominationRewardDTO reward: nominateDTO.getRewards()){
            Integer rewardID = reward.getId();
            Optional<RewardDTO> rewardCheck = rewardService.getRewardById(rewardID);
            if(rewardCheck.isEmpty())throw new ResourceNotFoundException("No such reward found");
            for(NomineeDTO nominee: reward.getNominees()){
                Optional<EmployeeDTO> nomineeCheck = employeeService.findActiveEmployeeById(nominee.getId());
                if(nomineeCheck.isEmpty())throw new ResourceNotFoundException("no such nominee found");
                String justification = "";
                if(!reward.getTeamJustification().isEmpty())justification = reward.getTeamJustification();
                if(!nominee.getInlineJustification().isEmpty())justification = nominee.getInlineJustification();
                if(nominatorCheck.get().getRole()>=rewardCheck.get().getRewardLevel()){
                    nominations.add(new NominationHelper(nominee.getId(), justification, rewardID));
                }
                else{
                    throw new NoAuthorisationException("You are not authorised to give this reward");
                }

            }
        }
        return nominateRepository.giveReward(nominatorId,nominations);
    }

}
