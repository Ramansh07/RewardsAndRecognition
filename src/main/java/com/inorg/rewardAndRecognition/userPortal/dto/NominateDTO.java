package com.inorg.rewardAndRecognition.userPortal.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominateDTO {

    @NotNull(message = "Nominator is mandatory")
    @JsonProperty("nominatorDetails")
    private NominatorDTO nominator;

    @JsonProperty("rewardDetails")
    private List<NominationRewardDTO> rewards;
}
