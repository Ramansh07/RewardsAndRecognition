package com.inorg.rewardAndRecognition.common.repository;
import com.inorg.rewardAndRecognition.common.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Integer> {
    public Optional<List<HistoryEntity>> findByUserId(String id) throws  Exception;
}
