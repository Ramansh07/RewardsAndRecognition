package com.inorg.rewardAndRecognition.common.repository;

import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.userPortal.dto.NominationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominationRepository extends JpaRepository<NominationEntity, Long> {
    List<NominationEntity> findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(List<Integer> nominationIds);
}
