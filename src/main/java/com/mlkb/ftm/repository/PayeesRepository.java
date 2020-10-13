package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PayeesRepository extends JpaRepository<Payee, Long> {
    @Query("SELECT payees FROM User WHERE email = ?1")
    Set<Payee> getPayees(String userMail);


}
