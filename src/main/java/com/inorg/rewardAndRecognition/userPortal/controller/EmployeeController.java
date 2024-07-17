package com.inorg.rewardAndRecognition.userPortal.controller;

import com.inorg.rewardAndRecognition.adminPortal.service.ApprovalService;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetDescriptionDTO;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import com.inorg.rewardAndRecognition.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
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
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    public EmployeeController(EmployeeService employeeService, ApprovalService approvalService) {
        this.employeeService = employeeService;
        this.approvalService = approvalService;
    }

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> getEmployees() throws Exception {
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

    @PostMapping(path = "employee/post-description")
    public ResponseEntity<ResponseDTO> postDescription(HttpServletRequest request, @RequestBody SetDescriptionDTO descriptionDTO) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.build(true,
                "Description posted successfully",
                LocalDateTime.now(),
                employeeService.postDescription(userId, descriptionDTO),
                null));
    }

    @GetMapping("/employee/pastWins")
    public ResponseEntity<ResponseDTO> getEmployeeHistory(HttpServletRequest request) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String adminId = jwtTokenProvider.getUsername(token);
        return ResponseEntity.ok(ResponseDTO.build(true,
                "Employee history retrieved successfully",
                LocalDateTime.now(),
                approvalService.findEmployeeHistory(adminId),
                null));
    }
}
