package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewardLevel_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardLevelMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String levelName;
    private int level;
}

