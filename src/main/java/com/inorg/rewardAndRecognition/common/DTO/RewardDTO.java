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

    @JsonProperty("rewardID")
    private int rewardId;

    @JsonProperty("points")
    private Integer points;

    @JsonProperty("rewardName")
    private String rewardName;

    @JsonProperty("rewardLevel")
    private int rewardLevel;
}
