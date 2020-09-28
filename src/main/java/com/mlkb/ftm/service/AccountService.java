package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidAccount(AccountDTO accountDTO) {
        return accountDTO != null
                && accountDTO.getId() != null
                && checkEmail(userDTO.getEmail())
                && checkPassword(userDTO.getPassword());
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
            throw new IllegalArgumentException("User with such email does not exist");
        }
    }
}
