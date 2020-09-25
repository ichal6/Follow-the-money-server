package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.ActivityDTO;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private User user;

    public DashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DashboardDTO getDashboard(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            return new DashboardDTO.Builder()
                    .withTotalBalance(getTotalBalance())
                    .withDifference(getDifferenceFromLast30Days())
                    .withPopularAccounts(getPopularAccounts())
                    .withRecentActivities(getLastFourActivity())
                    .withIncomeFunds(getIncomeFromLast12Months())
                    .withExpenseFunds(getExpenseFromLast12Months())
                    .build();
        } else {
            throw new IllegalArgumentException("Couldn't find a dashboard for user with give email");
        }
    }

    private TreeMap<Month, Double> getExpenseFromLast12Months() {
        Set<Account> accounts = user.getAccounts();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date previousYear = cal.getTime();

        return accounts.stream()
                .flatMap(
                        account -> account.getTransactions().stream())
                .filter(transaction -> transaction.getType() == GeneralType.EXPENSE)
                .filter(transaction -> transaction.getDate().getTime() > previousYear.getTime())
                .collect(groupingBy(transaction -> Month.from(transaction.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate()),
                        TreeMap::new,
                        Collectors.summingDouble(Transaction::getAbsoluteValue)));
    }

    private TreeMap<Month, Double> getIncomeFromLast12Months() {
        Set<Account> accounts = user.getAccounts();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date previousYear = cal.getTime();

        return accounts.stream()
                .flatMap(
                        account -> account.getTransactions().stream())
                .filter(transaction -> transaction.getType() == GeneralType.INCOME)
                .filter(transaction -> transaction.getDate().getTime() > previousYear.getTime())
                .collect(groupingBy(transaction -> Month.from(transaction.getDate().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate()),
                        TreeMap::new,
                        Collectors.summingDouble(Transaction::getValue)));
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

    private List<ActivityDTO> getLastFourActivity(){
        Set<Account> accounts = user.getAccounts();
        final int LIMIT = 4;

        List<ActivityDTO> activityDTOList = accounts.stream()
                .map(Account::getTransactions)
                .flatMap(transactions -> transactions.stream()
                        .map(transaction -> createActivityDTOFromTransaction(transaction, accounts)))
                .collect(Collectors.toList());

        activityDTOList.addAll(accounts.stream()
                .flatMap(account -> account.getTransfersFrom().stream())
                .map(this::createActivityDTOFromTransfer)
                .collect(Collectors.toList()));

        activityDTOList.sort(Comparator.comparing(ActivityDTO::getDate).reversed());
        return activityDTOList.stream()
                .limit(LIMIT)
                .collect(Collectors.toList());
    }

    private ActivityDTO createActivityDTOFromTransaction(Transaction transaction, Set<Account> accounts){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(transaction.getId());
        activityDTO.setTitle(transaction.getTitle());
        activityDTO.setCost(transaction.getValue());
        activityDTO.setDate(transaction.getDate());
        activityDTO.setPayeeTo(transaction.getPayee().getName());
        String payeeFrom = accounts.stream()
                .filter(account -> account.getTransactions().contains(transaction))
                .findFirst().get().getName();
        activityDTO.setPayeeFrom(payeeFrom);

        return activityDTO;
    }

    private ActivityDTO createActivityDTOFromTransfer(Transfer transfer){
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(transfer.getId());
        activityDTO.setTitle(transfer.getTitle());
        activityDTO.setCost(transfer.getValue());
        activityDTO.setDate(transfer.getDate());
        activityDTO.setPayeeTo(transfer.getAccountTo().getName());
        activityDTO.setPayeeFrom(transfer.getAccountFrom().getName());

        return activityDTO;
    }
}
