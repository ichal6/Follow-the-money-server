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

    @Query("SELECT CASE WHEN COUNT(tr) > 0 THEN true ELSE false END " +
            "FROM Account ac JOIN Transaction tr JOIN User u WHERE tr.id = ?1 AND u.email = ?2")
    Boolean existsByTransactionIdAndUserEmail(Long Id, String email);

}
