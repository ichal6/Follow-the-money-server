package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.entity.Currency;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.repository.AccountRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;
    private final AccountRepository accountRepository;

    public AccountService(UserRepository userRepository, InputValidator inputValidator, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
        this.accountRepository = accountRepository;
    }

    public boolean isValidNewAccount(AccountDTO accountDTO) throws InputIncorrectException {
        return accountDTO != null
                && inputValidator.checkName(accountDTO.getName())
                && inputValidator.checkIfAccountTypeInEnum(accountDTO.getAccountType())
                && inputValidator.checkBalance(accountDTO.getCurrentBalance());
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

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setStartingBalance(accountDTO.getCurrentBalance());
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setAccountType(Enum.valueOf(AccountType.class, accountDTO.getAccountType()));
        account.setCurrency(Currency.USD);

        Account savedAccount = accountRepository.save(account);
        accountDTO.setId(savedAccount.getId());

        return accountDTO;
    }
}
