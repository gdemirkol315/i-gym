package com.igym.repository;

import com.igym.entity.EntryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryProductRepository extends JpaRepository<EntryProduct, Long> {
}
