package com.inorg.rewardAndRecognition.common.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
public class SetDescriptionDTO {
    @NonNull
    @JsonProperty("EmployeeDescription")
    private String description;
}
