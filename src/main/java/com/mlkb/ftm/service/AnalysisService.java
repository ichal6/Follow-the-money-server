package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.AnalysisFinancialTableDTO;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AnalysisService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }


    public Set<AnalysisFinancialTableDTO> getTableData(String email, Instant dateStart,
                                                                   AnalysisFinancialTableDTO.AnalysisType type) {
        switch (type) {
            case accounts -> {
                return getTableDataTypeAccounts(email, dateStart);
            }
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
                .getMapTransactionsValueForPayee(payees, GeneralType.EXPENSE, dateStart);
        Map<String, BigDecimal> valueIncomeMap = this.transactionRepository
                .getMapTransactionsValueForPayee(payees, GeneralType.INCOME, dateStart);

        return  payees.stream().map(p -> AnalysisFinancialTableDTO.builder()
                    .name(p.getName())
                    .expense(valueExpenseMap.getOrDefault(p.getName(), BigDecimal.ZERO))
                    .income(valueIncomeMap.getOrDefault(p.getName(), BigDecimal.ZERO))
                    .build())
                .collect(Collectors.toSet());
    }

    public Set<AnalysisFinancialTableDTO> getTableDataTypeAccounts(String email, Instant dateStart) {
        User user = getUser(email);
        Set<Account> accounts = user.getAccounts();
        return accounts.stream()
                .filter(Account::getIsEnabled)
                .map(a -> AnalysisFinancialTableDTO.builder()
                    .name(a.getName())
                    .income(getValueFromTransactions(a.getTransactions(), GeneralType.INCOME, dateStart))
                    .expense(getValueFromTransactions(a.getTransactions(), GeneralType.EXPENSE, dateStart))
                    .build())
                .collect(Collectors.toSet());
    }

    public HashSet getTableDataTypeCategories(String email, Instant dateStart) {
        User user = getUser(email);
        Set<Category> categories = user.getCategories();
        return categories.stream()
                .map(c -> AnalysisFinancialTableDTO.builder()
                        .name(c.getName())
                        .income(getValueFromTransactions(transactionRepository.findByCategoryId(c.getId()), GeneralType.INCOME, dateStart))
                        .expense(getValueFromTransactions(transactionRepository.findByCategoryId(c.getId()), GeneralType.EXPENSE, dateStart))
                        .build())
                .collect(Collectors.collectingAndThen(Collectors.toMap(AnalysisFinancialTableDTO::getName, Function.identity(), this::mergeAnalysis),
                        m -> new HashSet(m.values()))
                );
    }

    private AnalysisFinancialTableDTO mergeAnalysis(AnalysisFinancialTableDTO a, AnalysisFinancialTableDTO b) {
        BigDecimal expense, income;
        if (a.getExpense().compareTo(b.getExpense()) > 0) {
            expense = a.getExpense();
            income = b.getIncome();
        } else {
            expense = b.getExpense();
            income = a.getIncome();
        }

       return AnalysisFinancialTableDTO.builder()
               .name(a.getName())
               .expense(expense)
               .income(income)
               .build();
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

    private BigDecimal getValueFromTransactions(Set<Transaction> transactions, GeneralType type, Instant dateStart) {
        Double value = transactions.stream()
                .filter(t -> t.getDate().after(Date.from(dateStart)))
                .filter(t -> t.getType().equals(type))
                .map(t -> Math.abs(t.getValue()))
                .reduce(0.0, Double::sum);
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
