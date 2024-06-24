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
public class RewardDTO {

    @JsonProperty("RewardID")
    private int rewardId;

    @JsonProperty("Points")
    private Integer points;

    @JsonProperty("RewardName")
    private String rewardName;

    @JsonProperty("RewardLevel")
    private int rewardLevel;

}
