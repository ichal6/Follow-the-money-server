package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public PaymentService(UserRepository userRepository, InputValidator inputValidator,
                          TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<PaymentDTO> getPaymentsWithParameters(String email, String accountId, String period) {
        try {
            Long accountIdLong = Long.parseLong(accountId);
            int periodInDays = Integer.parseInt(period);
            Optional<User> userOptional = userRepository.findByEmail(email);
            Optional<Account> accountOptional = accountRepository.findById(accountIdLong);
            if (userOptional.isPresent() && accountOptional.isPresent()) {
                User user = userOptional.get();
                Account account = accountOptional.get();
                if (user.getAccounts().contains(account)) {
                    return extractPaymentsForParameters(email, accountIdLong, periodInDays);
                } else {
                    throw new ResourceNotFoundException("User with given email doesn't have an account with given id");
                }
            } else {
                throw new ResourceNotFoundException("Couldn't find user or account for given parameters");
            }
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Given parameters for transactions are incorrect");
        }
    }

    private List<PaymentDTO> extractPaymentsForParameters(String email, Long accountId, int periodInDays) {
        List<PaymentDTO> payments = new ArrayList<>();
        //add logic
        return payments;
    }
}

