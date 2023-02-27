package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.AnalysisFinancialTableDTO;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    private final UserRepository userRepository;
    private User user;

    public AnalysisService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<AnalysisFinancialTableDTO> getTableData(String email) {
        if(!this.userRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException("Couldn't find a user for email: " + email);
        }
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            this.user = optionalUser.get();
        } else {
            throw new ResourceNotFoundException("Couldn't find a analysis for user with given email");
        }
        Set<Account> accounts = user.getAccounts();
        return accounts.stream().map(a -> AnalysisFinancialTableDTO.builder()
                .name(a.getName())
                .income(getValueFromTransactions(a, GeneralType.INCOME))
                .expense(getValueFromTransactions(a, GeneralType.EXPENSE))
                .build()).collect(Collectors.toSet());
    }

    private BigDecimal getValueFromTransactions(Account account, GeneralType type) {
        Double value = account.getTransactions().stream()
                .filter(t -> t.getType().equals(type))
                .map(t -> Math.abs(t.getValue()))
                .reduce(0.0, Double::sum);
        return new BigDecimal(value.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
