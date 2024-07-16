package com.inorg.rewardAndRecognition.common.service;

import com.inorg.rewardAndRecognition.common.entity.NominationEntity;
import com.inorg.rewardAndRecognition.common.repository.NominationRepository;
import com.inorg.rewardAndRecognition.config.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class NominationServiceCommon {
    @Autowired
    private NominationRepository nominationRepository;

    public List<NominationEntity> findByNominatorId(String id) throws ResourceNotFoundException {
        Optional<List<NominationEntity>> optionalNominationEntities = nominationRepository.findByNominatorId(id);
        if(optionalNominationEntities.isEmpty())throw new ResourceNotFoundException("The nominator has no current nominations");
        return  optionalNominationEntities.get();
    }
    public List<NominationEntity> findByNomineeIdAndStatus(String id, int status) throws ResourceNotFoundException {
        Optional<List<NominationEntity>> optionalNominationEntities = nominationRepository.findByNomineeIdAndStatus(id, status);
        if(optionalNominationEntities.isEmpty())throw new ResourceNotFoundException("The nominator has no current nominations");
        return  optionalNominationEntities.get();
    }

    public List<NominationEntity> findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(List<Integer>nominationIds) throws ResourceNotFoundException {
        Optional<List<NominationEntity>> nominations = nominationRepository.findByNominationIdInAndIsActiveTrueAndIsDeletedFalse(nominationIds);
        if (nominations.isEmpty()) {
            throw new ResourceNotFoundException("No nominations found");
        }
        return nominations.get();
    }
}
