package com.inorg.rewardAndRecognition.userPortal.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class NominationRewardDTO {
    @NotNull(message = "ID is mandatory")
    @JsonProperty("rewardId")
    private Integer id;
    @JsonProperty("rewardName")
    private String name;
    @JsonProperty("teamJustification")
    private String teamJustification;
    @JsonProperty("nomineeDetails")
    private List<NomineeDTO> nominees;

}
