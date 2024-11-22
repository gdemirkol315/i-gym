package com.igym.repository;

import com.igym.entity.EntryConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryConsumptionRepository extends JpaRepository<EntryConsumption, Long> {
    List<EntryConsumption> findByEntryProductTransactionId(Long entryProductTransactionId);
}
