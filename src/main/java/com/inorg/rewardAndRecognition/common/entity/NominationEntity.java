package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nominations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NominationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long nominationId;
    private String nominatorId;
    private String nomineeId;
    private Integer rewardId;
    private String justification;
    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;
    private Boolean isActive;
    private Boolean isDeleted;

}