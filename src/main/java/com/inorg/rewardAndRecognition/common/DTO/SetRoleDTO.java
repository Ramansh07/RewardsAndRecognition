package com.inorg.rewardAndRecognition.common.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SetRoleDTO {

    @JsonProperty("employeeId")
    private String employeeId;

    @JsonProperty("role")
    private int role;
}
