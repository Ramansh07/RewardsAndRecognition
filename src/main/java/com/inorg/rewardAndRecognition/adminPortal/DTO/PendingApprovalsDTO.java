package com.inorg.rewardAndRecognition.adminPortal.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Builder
public class PendingApprovalsDTO {

    private Long approvalId;
    private String approvalLevel;
    private String nominatorName;
    private String nomineeName;
    private String justificationForNomination;
    private String rewardName;
    private String justificationForApprovalDenial;
}
