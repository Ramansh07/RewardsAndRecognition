package com.inorg.rewardAndRecognition.userPortal.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class NomineeDTO {
    @NotNull(message = "ID is mandatory")
    @JsonProperty("nomineeId")
    private String id;
    @JsonProperty("nomineeName")
    private String name;
    @JsonProperty("nomineeJustification")
    private String inlineJustification;
}
