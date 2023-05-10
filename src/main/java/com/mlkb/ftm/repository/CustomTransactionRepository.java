package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.PaymentType;
import com.mlkb.ftm.entity.Payee;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Set;

public interface CustomTransactionRepository {
    Map<String, BigDecimal> getMapTransactionsValueForPayee(Set<Payee> payees, PaymentType type, Instant dateStart);
}
