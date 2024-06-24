package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CustomRewardRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RewardsEntity> findAllActiveRewards() throws Exception {
        String queryText = "SELECT r FROM RewardsEntity r WHERE r.isActive = true AND r.isDeleted = false";
        TypedQuery<RewardsEntity> query = entityManager.createQuery(queryText, RewardsEntity.class);
        return query.getResultList();
    }
    public RewardsEntity findActiveRewardById(String rewardId) throws Exception {
        String queryText = "SELECT e FROM RewardsEntity e WHERE e.isActive = true AND e.isDeleted = false AND e.rewardId = :rewardId";
        TypedQuery<RewardsEntity> query = entityManager.createQuery(queryText, RewardsEntity.class);
        query.setParameter("rewardId", rewardId);
        return query.getSingleResult();

    }

}
