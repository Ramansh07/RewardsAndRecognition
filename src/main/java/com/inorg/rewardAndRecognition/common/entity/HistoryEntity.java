package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Entity
@Table(name = "history_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "reward_id", nullable = false)
    private int rewardId;

    @Column(name = "nomination_id", nullable = false)
    private int nominationId;

    @Column(name = "reward_date")
    private LocalDateTime rewardDate;

    @Column(name = "cdn")
    private String cdn;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @Column(name = "updated_date_time")
    private LocalDateTime updatedDateTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;
}
