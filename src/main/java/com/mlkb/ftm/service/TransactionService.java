package com.mlkb.ftm.service;

import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository, InputValidator inputValidator, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
        this.transactionRepository = transactionRepository;
    }
}
