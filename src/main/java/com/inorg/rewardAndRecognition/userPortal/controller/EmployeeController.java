package com.inorg.rewardAndRecognition.userPortal.controller;

import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.common.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/user-portal")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ApprovalService approvalService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ApprovalService approvalService) {
        this.employeeService = employeeService;
        this.approvalService = approvalService;
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> getEmployees() throws Exception {
        System.out.println("\n\ninside get employees\n\n");
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employees retrieved successfully",
                LocalDateTime.now(),
                employeeService.findAllActiveEmployees(),
                null));
    }
    @GetMapping("/employeeRoleMapping")
    public ResponseEntity<ResponseDTO> getEmployeeRoleMapping() throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee Roles retrieved successfully",
                LocalDateTime.now(),
                employeeService.getEmployeeRoleMapping(),
                null));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResponseDTO> getEmployee(@PathVariable String employeeId) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee retrieved successfully",
                LocalDateTime.now(),
                employeeService.findActiveEmployeeById(employeeId),
                null));
    }

    @GetMapping("/employee")
    public ResponseEntity<ResponseDTO> getEmployeeByEmail(@RequestParam String employeeEmail) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee retrieved successfully",
                LocalDateTime.now(),
                employeeService.findActiveEmployeeByEmail(employeeEmail),
                null));
    }

    @PostMapping(path = "/employee/{employeeId}/post-description")
    public ResponseEntity<ResponseDTO> postDescription(@PathVariable String employeeId, @RequestBody SetDescriptionDTO descriptionDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.build(true,
                "Description posted successfully",
                LocalDateTime.now(),
                employeeService.postDescription(employeeId, descriptionDTO),
                null));
    }

    @GetMapping("/employee/{employeeId}/pastWins")
    public ResponseEntity<ResponseDTO> getEmployeeHistory(@PathVariable String employeeId) throws Exception {
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee history retrieved successfully",
                LocalDateTime.now(),
                approvalService.findEmployeeHistory(employeeId),
                null));
    }
}
