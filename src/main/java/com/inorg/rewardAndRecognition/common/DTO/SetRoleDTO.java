package com.inorg.rewardAndRecognition.common.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SetRoleDTO {

    @JsonProperty("employeeID")
    private String employeeId;

    @JsonProperty("Role")
    private int role;
}
