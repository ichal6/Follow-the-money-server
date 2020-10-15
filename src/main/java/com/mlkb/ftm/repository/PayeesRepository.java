package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Payee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

@Repository
public interface PayeesRepository extends JpaRepository<Payee, Long> {
    @Query("SELECT payees FROM User WHERE email = ?1")
    Set<Payee> getPayees(String userMail);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payee SET is_enabled = false WHERE payee.id = ?", nativeQuery = true)
    void setDisabled(Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO payee (general_type, name, user_id)  VALUES (?, ?, ?)", nativeQuery = true)
    void addPayee(String generalType, String name, Long userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE payee SET name = ? WHERE id = ?", nativeQuery = true)
    void updatePayee(String name, Long payeeId);

}
