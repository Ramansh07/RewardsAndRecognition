package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rewards")
public class RewardsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RewardID", nullable = false, unique = true)
    private int rewardId;

    @Column(name = "Points", nullable = false)
    private int points;

    @Column(name = "RewardName", length = 255, nullable = false)
    private String rewardName;

    @CreationTimestamp
    @Column(name = "CreatedDateTime", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    @Column(name = "LastModifiedDateTime", columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "IsActive",columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive;

    @Column(name = "LastModifiedBy")
    private String lastModifiedBy;

    @Column(name = "CreatedBy",  columnDefinition = "VARCHAR(255) DEFAULT 'system'")
    private String createdBy; // Default value 'system'

    @Column(name = "IsDeleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @Column(name = "Level", nullable = false)
    private int  level;
}
