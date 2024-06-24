package com.inorg.rewardAndRecognition.userPortal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Email;

import java.time.LocalDateTime;

@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empid", length = 50, nullable = false, unique = true)
    private String empId;

    @NonNull
    @Email
    @Column(name = "Email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "UserName", length = 50, nullable = false)
    private String userName;

    @Column(name = "Phone", length = 50, nullable = false)
    private Integer phoneNumber;

    @Column(name = "Description")
    private String description;

    @Column(name = "Points")
    private Integer points;

    @Column(name = "IsActive")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "CreatedDateTime", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "CreatedBy")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "LastModifiedDateTime", nullable = false)
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "LastModifiedBy")
    private String lastModifiedBy;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Column(name = "roleId")
    private int role;
}

