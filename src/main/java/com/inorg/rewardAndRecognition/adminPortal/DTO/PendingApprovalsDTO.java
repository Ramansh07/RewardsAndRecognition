package com.inorg.rewardAndRecognition.adminPortal.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingApprovalsDTO {
    private String nominatorName;
    private String nomineeName;
    private String justification;
    private String rewardName;
}
