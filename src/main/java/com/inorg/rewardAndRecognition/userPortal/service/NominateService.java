package com.inorg.rewardAndRecognition.userPortal.service;
import com.inorg.rewardAndRecognition.userPortal.dto.EmployeeDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
import com.inorg.rewardAndRecognition.userPortal.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.userPortal.repository.NominateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        String nominatorId = nominateDTO.getNominatorEmpId();
        List<String> nomineeIds = nominateDTO.getNomineeEmpIds();
        Integer rewardID = nominateDTO.getRewardId();
        String justification = nominateDTO.getJustification();
        EmployeeDTO nominatorDto = userPortalService.FindActiveEmployeeById(nominatorId);
        for (String nomineeId : nomineeIds) {
            EmployeeDTO nomineeDto = userPortalService.FindActiveEmployeeById(nomineeId);
        }
        RewardDTO rewardDto = rewardsService.getActiveRewardById(rewardID);

        if(nominatorDto.getRole()>=rewardDto.getRewardLevel()){
            return nominateRepository.giveReward(nominatorId,nomineeIds,rewardID, justification);
        }
        else{
            throw new NoAuthorisationException("You are not authorised to give this reward");
        }

    }

}
