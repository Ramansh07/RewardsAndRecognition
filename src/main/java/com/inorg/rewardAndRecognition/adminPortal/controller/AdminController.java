////package com.inorg.rewardAndRecognition.adminPortal.controller;
////
////public class AdminController {
////
////}
//package com.inorg.rewardAndRecognition.adminPortal.controller;
//import com.inorg.rewardAndRecognition.userPortal.dto.NominateDTO;
//import com.inorg.rewardAndRecognition.userPortal.dto.RewardDTO;
//import com.inorg.rewardAndRecognition.userPortal.dto.SetDescriptionDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//
//
//@RestController
//@RequestMapping(path = "/admin-portal")
//public class AdminController {
//
//    private final AdminPortalService adminPortalService;
//
//    @Autowired
//    public AdminController(AdminPortalService adminPortalService) {
//        this.adminPortalService = adminPortalService;
//    }
//
//    @GetMapping("/pending-requests")
//    public ResponseEntity<List<NominationDTO>> getPendingRequests() throws Exception {
//        List<NominationDTO> pendingRequests = adminPortalService.FindAllActivePendingRequests();
//        return ResponseEntity.ok().body(pendingRequests);
//    }
//    @PostMapping(path = "/employee/{employeeId}/post-description")
//    public ResponseEntity<String> postDescription(@PathVariable String employeeId, @RequestBody SetDescriptionDTO descriptionDTO)throws Exception{
//        boolean success = userPortalService.postDescription(employeeId, descriptionDTO);
//        if(success)return new ResponseEntity<>("setting description for "+ employeeId + "successful", HttpStatus.CREATED);
//        else return new ResponseEntity<>("failed in setting description for "+ employeeId , HttpStatus.NOT_IMPLEMENTED);
//    }
//    @GetMapping("/rewards")
//    public ResponseEntity<List<RewardDTO>> getRewards() throws Exception {
//        List<RewardDTO> rewards = userPortalService.getAllRewards();
//        return ResponseEntity.ok().body(rewards);
//    }
//    //getActiveRewardById
//    @GetMapping("/reward/{rewardId}")
//    public ResponseEntity<RewardDTO> getActiveRewardById(@PathVariable int rewardId) throws Exception {
//        RewardDTO reward = userPortalService.getActiveRewardById(rewardId);
//        return ResponseEntity.ok().body(reward);
//    }
//
//    @PostMapping("/nominate")
//    public ResponseEntity<String> Nominate(@RequestBody NominateDTO nominateDTO) throws Exception{
//
//        boolean success = userPortalService.giveReward(nominateDTO);
//        if(success)return new ResponseEntity<>("Nomination Successful", HttpStatus.CREATED);
//        else return new ResponseEntity<>("Nomination not Successful ", HttpStatus.NOT_IMPLEMENTED);
//    }
//}
