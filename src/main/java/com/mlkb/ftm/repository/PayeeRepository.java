package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {
    @Query("SELECT payees FROM User WHERE email = ?1")
    Set<Payee> getPayees(String userMail);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payee SET is_enabled = false WHERE payee.id = ?", nativeQuery = true)
    void setDisabled(Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO payee (name, user_id)  VALUES (?, ?)", nativeQuery = true)
    void addPayee(String name, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payee SET name = ? WHERE id = ?", nativeQuery = true)
    void updatePayee(String name, Long payeeId);

    @Query("SELECT pa FROM User u INNER JOIN u.payees pa WHERE pa.id = ?1 AND u.email = ?2")
    Optional<Payee> findByPayeeIdAndUserEmail(Long payeeId, String email);

}
