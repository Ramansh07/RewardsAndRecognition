package com.inorg.rewardAndRecognition.userPortal.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Data
public class RewardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "RewardID", nullable = false, unique = true)
    private int rewardId;

    @Column(name = "Points", nullable = false)
    private int points;

    @Column(name = "RewardName", length = 255, nullable = false)
    private String rewardName;

    @CreationTimestamp
    @Column(name = "CreatedDateTime", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "LastModifiedDateTime", nullable = false, columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "IsActive", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;

    @Column(name = "LastModifiedBy", nullable = false)
    private String lastModifiedBy;

    @Column(name = "CreatedBy", nullable = false)
    private String createdBy;

    @Column(name = "IsDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    //level describes to what role employee is that reward visible
    @Column(name = "Level", nullable = false)
    private int  level;
}
