package com.inorg.rewardAndRecognition.common.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
