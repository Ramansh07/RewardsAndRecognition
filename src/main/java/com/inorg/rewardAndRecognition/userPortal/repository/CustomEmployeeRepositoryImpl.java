package com.inorg.rewardAndRecognition.userPortal.repository;
import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.userPortal.exceptions.ResourceNotFoundException;
import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository{

    @PersistenceContext
    private EntityManager entityManager;

    public List<EmployeeEntity> findActiveEmployees(){
        String queryText = "SELECT e FROM EmployeeEntity e WHERE e.isActive = true AND e.isDeleted = false";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(queryText, EmployeeEntity.class);
        return query.getResultList();

    }

    public EmployeeEntity findActiveEmployeeById(String employeeId) throws Exception{
        String queryText = "SELECT e FROM EmployeeEntity e WHERE e.empId = :employeeId AND e.isActive = true AND e.isDeleted = false";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(queryText, EmployeeEntity.class);
        query.setParameter("employeeId", employeeId);
        try {
            EmployeeEntity resp = query.getSingleResult();
            return resp;
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Employee not found for id: " + employeeId);
        }

    }
    public EmployeeEntity findActiveEmployeeByEmail(String employeeEmail) throws Exception{
        String queryText = "SELECT e FROM EmployeeEntity e WHERE e.email = :employeeEmail AND e.isActive = true AND e.isDeleted = false";
        TypedQuery<EmployeeEntity> query = entityManager.createQuery(queryText, EmployeeEntity.class);
        query.setParameter("employeeEmail", employeeEmail);
        try {
            EmployeeEntity resp = query.getSingleResult();
            return resp;
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Employee not found for id: " + employeeEmail);
        } catch (NonUniqueResultException e) {
            throw new ResourceNotFoundException("Multiple active employees found for email: " + employeeEmail);
        }
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
