package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query( "SELECT ac " +
            "FROM User u INNER JOIN u.accounts ac WHERE ac.id = ?1 AND u.email = ?2")
    Optional<Account> findByAccountIdAndUserEmail(Long Id, String email);
}
