package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.PaymentType;
import com.mlkb.ftm.entity.Payee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomTransactionRepositoryImpl implements CustomTransactionRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, BigDecimal> getMapTransactionsValueForPayee(Set<Payee> payees, PaymentType type, Instant dateStart) {
        return this.entityManager.createQuery("""
    SELECT
        SUM(t.value) AS value, p.name AS name
    FROM Transaction t
    INNER JOIN Payee p ON p.id = t.payee.id
    WHERE t.type=:type AND p.isEnabled = true AND p IN :payees AND t.date > :date
    GROUP BY p.name
    """, Tuple.class)
                .setParameter("type", type)
                .setParameter("payees", payees)
                .setParameter("date", Date.from(dateStart), TemporalType.DATE)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                tuple -> (tuple.get("name")).toString(),
                                tuple -> new BigDecimal((tuple.get("value")).toString())
                                        .setScale(2, RoundingMode.HALF_UP).abs()
                        )
                );
    }
}
