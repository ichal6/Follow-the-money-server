package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
                    return extractPaymentsForParameters(account, periodInDays);
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

    private List<PaymentDTO> extractPaymentsForParameters(Account account, int periodInDays) {
        List<PaymentDTO> payments = new ArrayList<>();
        // extract payments from transactions
        // extract payments from transfers from
        // extract payments from transfers to
        // sort by date
        // calculate balance after each transaction
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date dateFrom = new Date(System.currentTimeMillis() - (periodInDays * DAY_IN_MS));


        List<PaymentDTO> transactions = account.getTransactions().stream()
                .filter(transaction -> transaction.getDate().getTime() > dateFrom.getTime())
                .map(transaction -> makePaymentDTOFromTransaction(transaction, account.getName()))
                .collect(Collectors.toList());

        payments.addAll(transactions);
        return payments;
    }

    private PaymentDTO makePaymentDTOFromTransaction(Transaction transaction, String accountName) {
        PaymentDTO newPaymentDTO = new PaymentDTO();
        newPaymentDTO.setInternal(false);
        newPaymentDTO.setId(transaction.getId());
        newPaymentDTO.setValue(transaction.getValue());
        newPaymentDTO.setDate(transaction.getDate());
        newPaymentDTO.setTitle(transaction.getTitle());
        if (transaction.getValue() > 0) {
            newPaymentDTO.setFrom(transaction.getPayee().getName());
            newPaymentDTO.setTo(accountName);
        } else {
            newPaymentDTO.setFrom(accountName);
            newPaymentDTO.setTo(transaction.getPayee().getName());
        }
        return newPaymentDTO;
    }






//    private boolean isInternal;
//    private Long id;
//    private Double value;
//    private Date date;
//    private String title;
//    private String from;
//    private String to;
//    private Double balanceAfter;
}

