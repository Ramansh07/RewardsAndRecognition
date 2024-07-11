package com.inorg.rewardAndRecognition.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHistoryDTO {
    String nominatorName;
    String nominationJustification;
    String rewardName;
    LocalDateTime dateTime;
}
