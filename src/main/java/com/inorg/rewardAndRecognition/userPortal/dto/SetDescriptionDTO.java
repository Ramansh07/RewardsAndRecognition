package com.inorg.rewardAndRecognition.userPortal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor(staticName = "build")
public class SetDescriptionDTO {
    @NonNull
    @JsonProperty("EmployeeDescription")
    private String description;
}
