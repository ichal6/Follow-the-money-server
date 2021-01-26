package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Authorities;
import com.mlkb.ftm.entity.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authorities, Long> {
    Optional<Authorities> findByName(AuthorityType role);
}
