package com.example.mlkb.repository;

import com.example.mlkb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<User, Long> {

}
