package com.inorg.rewardAndRecognition.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeHistoryDTO {
    String nominatorName;
    String nominationJustification;
    String rewardName;
}
