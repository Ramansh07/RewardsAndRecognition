package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RewardsRepository extends JpaRepository<RewardsEntity, Long> {

    List<RewardsEntity> findByIsActiveTrueAndIsDeletedFalse();

    RewardsEntity findByRewardIdAndIsActiveTrueAndIsDeletedFalse(int rewardId);

    @Modifying
    @Query("UPDATE RewardsEntity r SET r.isDeleted = true, r.isActive = false WHERE r.id IN :ids")
    void softDeleteByIds(List<Long> ids);

    List<RewardsEntity> findByIdIn(List<Long> ids);
}
