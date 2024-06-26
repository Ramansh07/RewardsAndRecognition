package com.inorg.rewardAndRecognition.userPortal.service;
import com.inorg.rewardAndRecognition.userPortal.Utility.NominationHelper;
import com.inorg.rewardAndRecognition.userPortal.Utility.NomineeHelper;
import com.inorg.rewardAndRecognition.userPortal.dto.*;
import com.inorg.rewardAndRecognition.userPortal.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.userPortal.repository.NominateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NominateService {
    private final NominateRepository nominateRepository;
    private final RewardsService rewardsService;
    private final UserPortalService userPortalService;

    @Autowired
    public NominateService(UserPortalService userPortalService, RewardsService rewardsService, NominateRepository nominateRepository) {
        this.nominateRepository = nominateRepository;
        this.rewardsService = rewardsService;
        this.userPortalService = userPortalService;

    }
    public Boolean giveReward(NominateDTO nominateDTO) throws Exception {
        String nominatorId = nominateDTO.getNominator().getId();
        EmployeeDTO nominatorCheck = userPortalService.FindActiveEmployeeById(nominatorId);
        List<NominationHelper> nominations = new ArrayList<>();
        for(NominationRewardDTO reward: nominateDTO.getRewards()){
            Integer rewardID = reward.getId();
            RewardDTO rewardCheck = rewardsService.getActiveRewardById(rewardID);
            for(NomineeDTO nominee: reward.getNominees()){
                EmployeeDTO nomineeCheck = userPortalService.FindActiveEmployeeById(nominee.getId());
                String justification = "";
                if(!reward.getTeamJustification().isEmpty())justification = reward.getTeamJustification();
                if(!nominee.getInlineJustification().isEmpty())justification = nominee.getInlineJustification();
                if(nominatorCheck.getRole()>=rewardCheck.getRewardLevel()){
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
