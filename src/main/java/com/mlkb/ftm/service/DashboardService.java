package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.Transaction;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.repository.AccountsRepository;
import com.mlkb.ftm.repository.TransactionRepository;
import com.mlkb.ftm.repository.TransferRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private User user;

    public DashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<DashboardDTO> getDashboard(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<DashboardDTO> optionalDashboardDTO = Optional.empty();
        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            Double totalBalance = getTotalBalance();
            Double difference = getDifferenceFromLast30Days();
            List<AccountDTO> popularAccounts = getPopularAccounts();

            DashboardDTO dashboardDTO = new DashboardDTO(totalBalance, difference, popularAccounts);
            optionalDashboardDTO = Optional.of(dashboardDTO);
        }
        return optionalDashboardDTO;
    }

    private Double getDifferenceFromLast30Days() {
        long thirtyDays = 2592000000L;
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - thirtyDays);

        Set<Account> accounts = user.getAccounts();

        return accounts.stream().flatMap(account -> {
            return account.getTransactions().stream();
        }).filter(transaction -> {
            return transaction.getDate().
                    getTime() >= thirtyDaysAgo.getTime();
        }).mapToDouble(Transaction::getValue).reduce(0, Double::sum);
    }

    private Double getTotalBalance() {
        return user.getAccounts().stream()
                .mapToDouble(Account::getCurrentBalance)
                .reduce(0, Double::sum);
    }

    private List<AccountDTO> getPopularAccounts() {
        Set<Account> accounts = user.getAccounts();
        Map<AccountDTO, Date> accountsWithLastModifiedDate = new HashMap<>();
        long fiveYears = 157784760000L;
        Date fiveYearsAgo = new Date(System.currentTimeMillis() - fiveYears);
        final int LIMIT = 4;

        for (Account account : accounts) {
            Date timeOfLastModification;
            if (account.getTransactions().size() == 0) {
                timeOfLastModification = fiveYearsAgo;
            } else {
                timeOfLastModification = account.getTransactions().stream()
                        .map(Transaction::getDate).max(Date::compareTo).get();
            }
            AccountDTO accountDTO = createDTOFromAccount(account);
            accountsWithLastModifiedDate.put(accountDTO, timeOfLastModification);
        }
        return accountsWithLastModifiedDate.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private AccountDTO createDTOFromAccount(Account account) {
        AccountDTO newAccountDTO = new AccountDTO();
        newAccountDTO.setId(account.getId());
        newAccountDTO.setName(account.getName());
        newAccountDTO.setAccountType(account.getAccountType().toString());
        newAccountDTO.setCurrentBalance(account.getCurrentBalance());

        return newAccountDTO;
    }
}
