package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.AnalysisFinancialTableDTO;
import com.mlkb.ftm.repository.CategoryRepository;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public AnalysisService(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }


    public Set<AnalysisFinancialTableDTO> getTableData(String email, Instant dateStart,
                                                                   AnalysisFinancialTableDTO.AnalysisType type) {
        switch (type) {
            case categories -> {
                return getTableDataTypeCategories(email, dateStart);
            }
            case payees -> {
                return getTableDataTypePayee(email, dateStart);
            }
            default -> {
                return getTableDataTypeAccounts(email, dateStart);
            }
        }
    }

    private Set<AnalysisFinancialTableDTO> getTableDataTypePayee(String email, Instant dateStart) {
        User user = getUser(email);
        Set<Payee> payees = user.getPayees();
        Map<String, BigDecimal> valueExpenseMap = this.transactionRepository
                .getMapTransactionsValueForPayee(payees, PaymentType.EXPENSE, dateStart);
        Map<String, BigDecimal> valueIncomeMap = this.transactionRepository
                .getMapTransactionsValueForPayee(payees, PaymentType.INCOME, dateStart);

        return  payees.stream().map(p -> AnalysisFinancialTableDTO.builder()
                    .name(p.getName())
                    .expense(valueExpenseMap.getOrDefault(p.getName(), BigDecimal.ZERO))
                    .income(valueIncomeMap.getOrDefault(p.getName(), BigDecimal.ZERO))
                    .build())
                .collect(Collectors.toSet());
    }

    private Set<AnalysisFinancialTableDTO> getTableDataTypeAccounts(String email, Instant dateStart) {
        User user = getUser(email);
        Set<Account> accounts = user.getAccounts();
        return accounts.stream()
                .filter(Account::getIsEnabled)
                .map(a -> AnalysisFinancialTableDTO.builder()
                    .name(a.getName())
                    .income(getValueFromTransactions(a.getTransactions(), PaymentType.INCOME, dateStart))
                    .expense(getValueFromTransactions(a.getTransactions(), PaymentType.EXPENSE, dateStart))
                    .build())
                .collect(Collectors.toSet());
    }

    private Set<AnalysisFinancialTableDTO> getTableDataTypeCategories(String email, Instant dateStart) {
        Long userId = this.getUserId(email);
        Set<Category> categories = this.categoryRepository.findAllByOwnerId(userId);

        Map<String, BigDecimal> valueExpenseMap = this.transactionRepository
                .getMapTransactionValueForCategories(categories, PaymentType.EXPENSE, dateStart);
        Map<String, BigDecimal> valueIncomeMap = this.transactionRepository
                .getMapTransactionValueForCategories(categories, PaymentType.INCOME, dateStart);

        return categories.stream()
                .map(c -> AnalysisFinancialTableDTO.builder()
                        .name(c.getName())
                        .expense(valueExpenseMap.getOrDefault(c.getName(), BigDecimal.ZERO))
                        .income(valueIncomeMap.getOrDefault(c.getName(), BigDecimal.ZERO))
                        .build())
                .collect(Collectors.toSet());
    }

    public Instant convertParamToInstant(Optional<String> possibleDate) throws DateTimeParseException {
        return possibleDate
                .map(date -> LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC")).toInstant())
                .orElse(Instant.ofEpochMilli(0L));
    }

    private User getUser(String email) {
        if(!this.userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("Couldn't find a user for email: " + email);
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResourceNotFoundException("Couldn't find a analysis for user with given email");
        }
    }

    private Long getUserId(String email) {
        if(!this.userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(String.format("Couldn't find a user for email: %s", email));
        }
        return this.userRepository.getUserId(email);
    }

    private BigDecimal getValueFromTransactions(Set<Transaction> transactions, PaymentType type, Instant dateStart) {
        Double value = transactions.stream()
                .filter(t -> t.getDate().after(Date.from(dateStart)))
                .filter(t -> t.getType().equals(type))
                .map(t -> Math.abs(t.getValue()))
                .reduce(0.0, Double::sum);
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
