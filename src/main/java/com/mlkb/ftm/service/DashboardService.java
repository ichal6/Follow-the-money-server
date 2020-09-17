package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.Transfer;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.TransferRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;

    public DashboardService(UserRepository userRepository, TransactionRepository transactionRepository,
                            TransferRepository transferRepository){
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
    }

    public Optional<DashboardDTO> getDashboard(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<DashboardDTO> optionalDashboardDTO = Optional.empty();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Double totalBalance = getTotalBalance(user);
            Double difference = getDifferenceFromLast30Days(user);
            DashboardDTO dashboardDTO = new DashboardDTO(totalBalance, difference);
            optionalDashboardDTO = Optional.of(dashboardDTO);
        }
        return optionalDashboardDTO;
    }

    private Double getDifferenceFromLast30Days(User user){
        long thirtyDays = 2592000000L;
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - thirtyDays);

        Set<Account> accounts = user.getAccounts();

        double sumOfTransaction = accounts.stream().flatMap(account -> {
            return account.getTransactions().stream();
        }).filter(transaction -> {
            return transaction.getDate().
                    getTime() >= thirtyDaysAgo.getTime();
        }).mapToDouble(Transaction::getValue).reduce(0, Double::sum);

        sumOfTransaction += accounts.stream().flatMap(account -> {
            return account.getTransfersFrom().stream();
        }).filter(transaction -> {
            return transaction.getDate().
                    getTime() >= thirtyDaysAgo.getTime();
        }).mapToDouble(Transfer::getValue).reduce(0, Double::sum);

        return sumOfTransaction;
    }

    private Double getTotalBalance(User user){
        Double totalBalance = user.getAccounts().stream()
                .mapToDouble(Account::getCurrentBalance)
                .reduce(0, Double::sum);
        return totalBalance;
    }
}
