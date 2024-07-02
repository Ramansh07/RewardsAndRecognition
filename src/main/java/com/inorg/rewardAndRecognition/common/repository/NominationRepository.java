package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.userPortal.dto.NominationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NominationRepository extends JpaRepository<NominationEntity, Long> {
    Optional<List<NominationEntity>> findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(List<Integer> nominationIds);

    @Transactional
    @Modifying
    @Query("UPDATE NominationEntity n SET n.status = -1 WHERE n.nominationId = :nominationId")
    void markAsDenied(int nominationId);

    @Transactional
    @Modifying
    @Query("UPDATE NominationEntity n SET n.status = 1 WHERE n.nominationId = :nominationId")
    void markAsApproved(int nominationId);

    NominationEntity findByNominationId(Integer nominationId);
    Optional<List<NominationEntity>>findByNomineeIdAndStatus(String id, int status);
}
