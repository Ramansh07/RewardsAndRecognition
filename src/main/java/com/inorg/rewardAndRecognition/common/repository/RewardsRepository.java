package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.RewardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardsRepository extends JpaRepository<RewardsEntity, Long> {
    Optional<RewardsEntity> findByRewardNameAndIsActiveTrueAndIsDeletedFalse(String rewardName);

    Optional<List<RewardsEntity>> findByIsActiveTrueAndIsDeletedFalse();

    Optional<RewardsEntity> findByRewardIdAndIsActiveTrueAndIsDeletedFalse(int rewardId);

    @Modifying
    @Query("UPDATE RewardsEntity r SET r.isDeleted = true, r.isActive = false WHERE r.id IN :ids")
    int softDeleteByIds(@Param("ids") List<Long> ids);

    Optional<List<RewardsEntity>> findByRewardIdIn(List<Integer> ids);
}
