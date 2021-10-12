package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Override
    @Modifying
    @Query("DELETE FROM Transaction t where t.id = ?1")
    void deleteById(Long aLong);
}
