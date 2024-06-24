package com.inorg.rewardAndRecognition.userPortal.repository;

import com.inorg.rewardAndRecognition.userPortal.entity.EmployeeEntity;
import com.inorg.rewardAndRecognition.userPortal.entity.RewardsEntity;
import com.inorg.rewardAndRecognition.userPortal.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CustomRewardRepositoryImpl implements CustomRewardRepository{
    @PersistenceContext
    private EntityManager entityManager;

    public List<RewardsEntity> findAllActiveRewards() throws Exception {
        String queryText = "SELECT r FROM RewardsEntity r WHERE r.isActive = true AND r.isDeleted = false";
        TypedQuery<RewardsEntity> query = entityManager.createQuery(queryText, RewardsEntity.class);
        return query.getResultList();
    }


    public RewardsEntity findActiveRewardById(int rewardId) throws Exception {
        String queryText = "SELECT e FROM RewardsEntity e WHERE e.isActive = true AND e.isDeleted = false AND e.rewardId = :rewardId";
        TypedQuery<RewardsEntity> query = entityManager.createQuery(queryText, RewardsEntity.class);
        query.setParameter("rewardId", rewardId);
        try {
            RewardsEntity reward = query.getSingleResult();
            return reward;
        } catch (NoResultException e) {
            System.out.println("No reward found for id: " + rewardId);
            throw new ResourceNotFoundException("Reward not found for id: " + rewardId);
        }

    }

}
