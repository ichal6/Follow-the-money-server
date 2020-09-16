package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long> {
}
