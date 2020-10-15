package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {
}
