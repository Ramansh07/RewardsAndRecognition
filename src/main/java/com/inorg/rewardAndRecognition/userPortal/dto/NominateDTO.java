package com.inorg.rewardAndRecognition.userPortal.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominateDTO {
    @JsonProperty("rewardDetails")
    private List<NominationRewardDTO> rewards;
}
