package com.inorg.rewardAndRecognition.userPortal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominateDTO {
    @NotNull
    @JsonProperty("NominatorID")
    private String nominatorEmpId;

    @NonNull
    @JsonProperty("NomineeID")
    private List<String> nomineeEmpIds;

    @NotNull
    @JsonProperty("RewardID")
    private Integer rewardId;

    @JsonProperty("Justification")
    private String justification;
}
