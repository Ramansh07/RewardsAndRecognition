package com.inorg.rewardAndRecognition.adminPortal.DTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor(staticName = "build")
@Builder
@Data
public class ApprovalUpdateDTO {
    private Long approvalId;
    private int status;
    private String justification;
}