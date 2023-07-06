package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, CustomTransactionRepository {
    @Override
    @Modifying
    @Query("DELETE FROM Transaction t where t.id = ?1")
    void deleteById(Long aLong);

    @Query("SELECT CASE WHEN EXISTS " +
            "( SELECT 1 " +
                "FROM User u INNER JOIN u.accounts ac INNER JOIN ac.transactions tr WHERE tr.id = ?1 AND u.email = ?2" +
            ") THEN true ELSE false END")
    boolean existsByTransactionIdAndUserEmail(Long Id, String email);

}
