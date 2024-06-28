package com.inorg.rewardAndRecognition.adminPortal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRewardDTO {

    @NotNull
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("points")
    private Integer points;

    @JsonProperty("rewardName")
    private String rewardName;

    @JsonProperty("rewardLevel")
    private int rewardLevel;
}