package com.inorg.rewardAndRecognition.userPortal.service;

import com.inorg.rewardAndRecognition.userPortal.dto.EmployeeDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.userPortal.exceptions.NoAuthorisationException;
import com.inorg.rewardAndRecognition.userPortal.exceptions.ResourceNotFoundException;
import com.inorg.rewardAndRecognition.userPortal.exceptions.InvalidRequest;
import com.inorg.rewardAndRecognition.userPortal.repository.EmployeeRepository;
import com.inorg.rewardAndRecognition.userPortal.repository.NominateRepository;
import com.inorg.rewardAndRecognition.userPortal.repository.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPortalService {
    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    private final NominateRepository nominateRepository;

    @Autowired
    public UserPortalService(EmployeeRepository employeeRepository, RewardRepository rewardRepository, NominateRepository nominateRepository) {
        this.employeeRepository = employeeRepository;
        this.rewardRepository = rewardRepository;
        this.nominateRepository = nominateRepository;
    }

    public List<EmployeeDTO> FindAllActiveEmployees() throws Exception{
        System.out.println("\n\n inside FindAllActiveEmployees \n\n");

        List<EmployeeEntity> listEmployeeEntity = employeeRepository.findActiveEmployees();
        if(!listEmployeeEntity.isEmpty()){
            ArrayList<EmployeeDTO> resp =  new ArrayList<>();
            for (EmployeeEntity employeeEntity : listEmployeeEntity) {
                EmployeeDTO employeeDTO = EmployeeDTO.builder()
                        .empId(employeeEntity.getEmpId())
                        .email(employeeEntity.getEmail())
                        .userName(employeeEntity.getUserName())
                        .mobileNumber(employeeEntity.getPhoneNumber())
                        .description(employeeEntity.getDescription())
                        .points(employeeEntity.getPoints())
                        .role(employeeEntity.getRole())
                        .build();

                resp.add(employeeDTO);
            }
            System.out.println("\n\n exiting FindAllActiveEmployees \n\n");
            return resp;
        }
        else{
            throw new ResourceNotFoundException("There are no employees in the database right now");
        }
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
    public EmployeeDTO FindActiveEmployeeById(String id) throws Exception{
        System.out.println("\n\n inside getEmployee by id service layer \n\n");

        if(id == null || id.isEmpty()){
            throw new InvalidRequest("Id of employee cannot be null or blank");
        }
       EmployeeEntity employeeEntity = employeeRepository.findActiveEmployeeById(id);
       if(employeeEntity!=null) {
           System.out.println("\n\n inside   if(employeeEntity!=null) \n\n");
           EmployeeDTO employeeDTO = EmployeeDTO.builder()
                   .empId(employeeEntity.getEmpId())
                   .email(employeeEntity.getEmail())
                   .userName(employeeEntity.getUserName())
                   .mobileNumber(employeeEntity.getPhoneNumber())
                   .description(employeeEntity.getDescription())
                   .points(employeeEntity.getPoints())
                   .role(employeeEntity.getRole())
                   .build();
           return employeeDTO;


       }
       else{
           System.out.println("\n\n inside else \n\n");
           throw new ResourceNotFoundException("No Employee is present with the id" + id);
       }

    }
    public EmployeeDTO FindActiveEmployeeByEmail(String email) throws Exception{


        if(email == null || email.isEmpty()){
            throw new InvalidRequest("email of employee cannot be null or blank");
        }
        EmployeeEntity employeeEntity = employeeRepository.findActiveEmployeeByEmail(email);
        if(employeeEntity!=null) {
            System.out.println("\n\n inside   if(employeeEntity!=null) \n\n");
            EmployeeDTO employeeDTO = EmployeeDTO.builder()
                    .empId(employeeEntity.getEmpId())
                    .email(employeeEntity.getEmail())
                    .userName(employeeEntity.getUserName())
                    .mobileNumber(employeeEntity.getPhoneNumber())
                    .description(employeeEntity.getDescription())
                    .points(employeeEntity.getPoints())
                    .role(employeeEntity.getRole())
                    .build();
            return employeeDTO;


        }
        else{
            System.out.println("\n\n inside else \n\n");
            throw new ResourceNotFoundException("No Employee is present with the id" + email);
        }

    }
    public boolean postDescription(String id, SetDescriptionDTO descriptionDTO) throws Exception{
        if(id == null || id.isEmpty()){
            throw new InvalidRequest("Id of employee cannot be null or blank");
        }
        EmployeeDTO employeeDTO = FindActiveEmployeeById(id);
        return employeeRepository.updateDescription(id, descriptionDTO.getDescription());

    }
    public Boolean giveReward(NominateDTO nominateDTO) throws Exception {
        String nominatorId = nominateDTO.getNominatorEmpId();
        List<String> nomineeIds = nominateDTO.getNomineeEmpIds();
        Integer rewardID = nominateDTO.getRewardId();
        String justification = nominateDTO.getJustification();
        EmployeeDTO nominatorDto = FindActiveEmployeeById(nominatorId);
        for (String nomineeId : nomineeIds) {
            EmployeeDTO nomineeDto = FindActiveEmployeeById(nomineeId);
        }
        RewardDTO rewardDto = getActiveRewardById(rewardID);
        if(nominatorDto.getRole()>=rewardDto.getRewardLevel()){
            return nominateRepository.giveReward(nominatorId,nomineeIds,rewardID, justification);

        }
        else{
            throw new NoAuthorisationException("You are not authorised to give this reward");
        }

    }




}
