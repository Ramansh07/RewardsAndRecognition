package com.inorg.rewardAndRecognition.userPortal.service;

import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.userPortal.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.userPortal.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.userPortal.repository.NominateRepository;
import com.inorg.rewardAndRecognition.userPortal.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RewardsService {

        private final RewardRepository rewardRepository;

        @Autowired
        public RewardsService(EmployeeRepository employeeRepository, RewardRepository rewardRepository, NominateRepository nominateRepository) {
            this.rewardRepository = rewardRepository;
        }

        public List<RewardDTO> getAllRewards() throws Exception{

            List<RewardsEntity> listRewardsEntity= rewardRepository.findAllActiveRewards();
            if(!listRewardsEntity.isEmpty()){
                ArrayList<RewardDTO> resp =  new ArrayList<>();
                for (RewardsEntity rewardsEntity : listRewardsEntity) {
                    RewardDTO rewardDTO = RewardDTO.builder()
                            .rewardName(rewardsEntity.getRewardName())
                            .rewardId(rewardsEntity.getRewardId())
                            .points(rewardsEntity.getPoints())
                            .rewardLevel(rewardsEntity.getLevel())
                            .build();

                    resp.add(rewardDTO);
                }
                return resp;
            }
            else{
                throw new ResourceNotFoundException("There are no Rewards in the database right now");
            }
        }

        public RewardDTO getActiveRewardById(int rewardId) throws Exception{

            RewardsEntity rewardsEntity= rewardRepository.findActiveRewardById(rewardId);
            if(rewardsEntity!=null){
                RewardDTO rewardDTO = RewardDTO.builder()
                        .rewardName(rewardsEntity.getRewardName())
                        .rewardId(rewardsEntity.getRewardId())
                        .points(rewardsEntity.getPoints())
                        .rewardLevel(rewardsEntity.getLevel())
                        .build();

                return rewardDTO;
            }
            else{
                throw new ResourceNotFoundException("There are no Rewards in the database right now");
            }
        }

}
