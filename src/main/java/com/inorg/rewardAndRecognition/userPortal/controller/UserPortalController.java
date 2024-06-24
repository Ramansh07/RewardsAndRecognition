package com.inorg.rewardAndRecognition.userPortal.controller;


import com.inorg.rewardAndRecognition.userPortal.dto.EmployeeDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.service.UserPortalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequestMapping(path = "/user-portal")
public class UserPortalController {

    private final UserPortalService userPortalService;

    @Autowired
    public UserPortalController(UserPortalService userPortalService) {
        this.userPortalService = userPortalService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getEmployees() throws Exception {
        System.out.println("\n\n inside getEmployees \n\n");
        List<EmployeeDTO> employees = userPortalService.FindAllActiveEmployees();
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String employeeId) throws Exception {
        EmployeeDTO employee = userPortalService.FindActiveEmployeeById(employeeId);
        return ResponseEntity.ok().body(employee);
    }
    @PostMapping(path = "/employee/{employeeId}/post-description")
    public ResponseEntity<String> postDescription(@PathVariable String employeeId, @RequestBody SetDescriptionDTO descriptionDTO)throws Exception{
        System.out.println("\n\n inside postDescription \n\n");
        System.out.println("\n\n employeeId\n\n" +"   "+ employeeId +"   "+descriptionDTO.getDescription());
        boolean success = userPortalService.postDescription(employeeId, descriptionDTO);
        if(success)return new ResponseEntity<>("setting description for "+ employeeId + "successful", HttpStatus.CREATED);
        else return new ResponseEntity<>("failed in setting description for "+ employeeId , HttpStatus.NOT_IMPLEMENTED);
    }
    @GetMapping("/rewards")
    public ResponseEntity<List<RewardDTO>> getRewards() throws Exception {
        List<RewardDTO> rewards = userPortalService.getAllRewards();
        return ResponseEntity.ok().body(rewards);
    }
    //getActiveRewardById
    @GetMapping("/reward/{rewardId}")
    public ResponseEntity<RewardDTO> getActiveRewardById(@PathVariable int rewardId) throws Exception {
        RewardDTO reward = userPortalService.getActiveRewardById(rewardId);
        return ResponseEntity.ok().body(reward);
    }

    @PostMapping("/nominate")
    public ResponseEntity<String> Nominate(@RequestBody NominateDTO nominateDTO) throws Exception{
        boolean success = userPortalService.giveReward(nominateDTO);
        if(success)return new ResponseEntity<>("Nomination Successful", HttpStatus.CREATED);
        else return new ResponseEntity<>("Nomination not Successful ", HttpStatus.NOT_IMPLEMENTED);
    }

}
