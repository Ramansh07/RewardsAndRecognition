package com.inorg.rewardAndRecognition.userPortal.repository;
import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CustomEmployeeRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;


    public List<EmployeeEntity> findActiveEmployees() throws Exception {
        String queryText = "SELECT e FROM EmployeeEntity e WHERE e.isActive = true AND e.isDeleted = false";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(queryText, EmployeeEntity.class);
        return query.getResultList();

    }

    public EmployeeEntity findActiveEmployeeById(String employeeId) {
        String queryText = "SELECT e FROM EmployeeEntity e WHERE e.empId = :employeeId AND e.isActive = true AND e.isDeleted = false";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(queryText, EmployeeEntity.class);
        query.setParameter("employeeId", employeeId);
        return query.getSingleResult();

    }
    @Transactional
    public boolean updateDescription(String employeeId, String description) {
        String queryText = "UPDATE EmployeeEntity e SET e.description = :description WHERE e.empId = :employeeId";
        int updatedRows = entityManager.createQuery(queryText)
                .setParameter("description", description)
                .setParameter("employeeId", employeeId)
                .executeUpdate();
        return (updatedRows == 1);
    }

}
