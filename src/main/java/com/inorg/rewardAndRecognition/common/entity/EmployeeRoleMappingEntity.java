package com.inorg.rewardAndRecognition.common.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_role_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRoleMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String roleName;
    private int role;
}


