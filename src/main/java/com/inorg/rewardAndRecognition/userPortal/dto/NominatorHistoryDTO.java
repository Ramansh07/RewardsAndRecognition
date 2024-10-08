package com.inorg.rewardAndRecognition.userPortal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NominatorHistoryDTO {
    private String nomineeName;
    private String justificationForNomination;
    private int currentLevel;
    private String currentStatus;
    private String reasonForDenial;
    private LocalDateTime dateTime;
}
