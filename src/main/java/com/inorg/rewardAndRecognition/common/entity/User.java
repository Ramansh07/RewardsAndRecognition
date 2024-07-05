package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private String createdBy;
    private String updatedBy;
}
