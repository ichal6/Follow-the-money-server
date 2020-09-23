package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Account;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<AccountDTO> getAllAccountsFromUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            List<AccountDTO> accountDTOS = optionalUser.get().getAccounts().stream()
                    .sorted(Comparator.comparing(o -> o.getTransactions().size()))
                    .map(x -> new AccountDTO(x.getId(), x.getName(), x.getAccountType().toString(), x.getCurrentBalance()))
                    .collect(Collectors.toList());
            Collections.reverse(accountDTOS);
            return accountDTOS;
        } else {
            throw new NoSuchElementException("User with such email does not exist");
        }
    }
}
