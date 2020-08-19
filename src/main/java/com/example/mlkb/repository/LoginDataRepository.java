package com.example.mlkb.repository;

import com.example.mlkb.entity.LoginData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginDataRepository extends JpaRepository<LoginData, Long> {
    Optional<LoginData> findByEmail(String email);
}
