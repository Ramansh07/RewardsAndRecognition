package com.inorg.rewardAndRecognition.common.repository;
import com.inorg.rewardAndRecognition.common.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>{

    @Query("SELECT e FROM EmployeeEntity e WHERE e.isActive = true AND e.isDeleted = false")
    List<EmployeeEntity> findActiveEmployees();

    @Query("SELECT e FROM EmployeeEntity e WHERE e.isActive = true AND e.isDeleted = false AND e.empId = :id")
    EmployeeEntity findActiveEmployeeById(@Param("id") String id);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.isActive = true AND e.isDeleted = false AND e.email = :email")
    Optional<EmployeeEntity> findActiveEmployeeByEmail(@Param("email") String email);

    @Query("UPDATE EmployeeEntity e SET e.description = :description WHERE e.empId = :id AND e.isActive = true AND e.isDeleted = false")
    boolean updateDescription(@Param("id") String id, @Param("description") String description);

    @Query("SELECT e FROM EmployeeEntity e WHERE e.empId IN :employeeIds AND e.isActive = true AND e.isDeleted = false")
    List<EmployeeEntity> findActiveEmployeesByIds(@Param("employeeIds") List<String> employeeIds);
}
