package com.inorg.rewardAndRecognition.userPortal.controller;
import com.inorg.rewardAndRecognition.userPortal.dto.EmployeeDTO;
import com.inorg.rewardAndRecognition.userPortal.dto.SetDescriptionDTO;
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
        List<EmployeeDTO> employees = userPortalService.FindAllActiveEmployees();
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String employeeId) throws Exception {
        EmployeeDTO employee = userPortalService.FindActiveEmployeeById(employeeId);
        return ResponseEntity.ok().body(employee);
    }
    @GetMapping("/employeeByEmail/{employeeEmail}")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(@PathVariable String employeeEmail) throws Exception {
        EmployeeDTO employee = userPortalService.FindActiveEmployeeByEmail(employeeEmail);
        return ResponseEntity.ok().body(employee);
    }
    @PostMapping(path = "/employee/{employeeId}/post-description")
    public ResponseEntity<String> postDescription(@PathVariable String employeeId, @RequestBody SetDescriptionDTO descriptionDTO)throws Exception{
        boolean success = userPortalService.postDescription(employeeId, descriptionDTO);
        if(success)return new ResponseEntity<>("setting description for "+ employeeId + "successful", HttpStatus.CREATED);
        else return new ResponseEntity<>("failed in setting description for "+ employeeId , HttpStatus.NOT_IMPLEMENTED);
    }

}
