package com.inorg.rewardAndRecognition.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalId;

    @Column(name = "nominationid", nullable = false)
    private int nominationId;

    @Column(name = "approveeid", length = 255)
    private String approveeId;

    @Column(name = "approvallevel", nullable = false)
    private int approvalLevel;

    @Column(name = "approvalstatus", nullable = false)
    private int approvalStatus;
}
