package com.igym.repository;

import com.igym.entity.EntryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryProductRepository extends JpaRepository<EntryProduct, Long> {
    List<EntryProduct> findByEntryType(EntryProduct.EntryType entryType);
    List<EntryProduct> findByDurationIsNotNull();
    List<EntryProduct> findByMaxEntriesIsNotNull();
}
