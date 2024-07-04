package com.inorg.rewardAndRecognition.common.repository;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {
    List<ApprovalEntity> findByApprovalLevelAndApprovalStatus(int approvalLevel, int approvalStatus);

    @Query("SELECT a.justification FROM ApprovalEntity a WHERE a.nominationId = :nominationId AND a.approvalStatus = :approvalStatus")
    Optional<String> findJustificationByNominationIdAndApprovalStatus(@Param("nominationId") int nominationId, @Param("approvalStatus") int approvalStatus);
}