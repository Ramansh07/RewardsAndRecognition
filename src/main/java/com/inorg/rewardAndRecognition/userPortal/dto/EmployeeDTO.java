package com.inorg.rewardAndRecognition.userPortal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor(staticName = "build")
public class EmployeeDTO {


    @JsonProperty("employeeID")
    private String empId;

    @Email
    @JsonProperty("EmployeeEmail")
    private String email;

    @JsonProperty("EmployeeName")
    private String userName;

    @JsonProperty("EmployeeMobileNumber")
    private Integer mobileNumber;

    @JsonProperty("EmployeeDescription")
    private String description;

    @JsonProperty("EmployeePoints")
    private Integer points;

    @JsonProperty("Role")
    private Integer role;

}
