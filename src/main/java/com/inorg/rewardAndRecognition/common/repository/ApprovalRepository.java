package com.inorg.rewardAndRecognition.common.repository;
import com.inorg.rewardAndRecognition.common.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {
    List<ApprovalEntity> findByApprovalLevelAndApprovalStatus(int approvalLevel, int approvalStatus);
}