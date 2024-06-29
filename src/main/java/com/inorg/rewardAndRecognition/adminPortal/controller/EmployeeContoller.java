package com.inorg.rewardAndRecognition.adminPortal.controller;
import com.inorg.rewardAndRecognition.common.DTO.ResponseDTO;
import com.inorg.rewardAndRecognition.common.DTO.SetRoleDTO;
import com.inorg.rewardAndRecognition.common.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("admin-portal")
public class EmployeeContoller {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping(path = "/employee/{adminId}/change-roles")
    public ResponseEntity<ResponseDTO> postDescription(@RequestBody List<SetRoleDTO> setRoleDTOs) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.build(true,
                "roles changed successfully",
                LocalDateTime.now(),
                employeeService.putRoles(setRoleDTOs),
                null));
    }
}
