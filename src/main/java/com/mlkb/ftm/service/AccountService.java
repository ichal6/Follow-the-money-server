package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.entity.Currency;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
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

    public boolean isValidNewAccount(NewAccountDTO newAccountDTO) throws InputIncorrectException {
        return newAccountDTO != null
                && inputValidator.checkName(newAccountDTO.getName())
                && inputValidator.checkIfAccountTypeInEnum(newAccountDTO.getAccountType())
                && inputValidator.checkBalance(newAccountDTO.getCurrentBalance())
                && inputValidator.checkBalance(newAccountDTO.getStartingBalance());
    }

    public List<AccountDTO> getAllAccountsFromUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            List<AccountDTO> accountDTOS = optionalUser.get().getAccounts().stream()
                    .sorted(Comparator.comparing(o -> o.getTransactions().size()))
                    .filter(account -> account.getIsEnabled() == true)
                    .map(x -> new AccountDTO(x.getId(), x.getName(), x.getAccountType().toString(), x.getCurrentBalance(), x.getStartingBalance()))
                    .collect(Collectors.toList());
            Collections.reverse(accountDTOS);
            return accountDTOS;
        } else {
            throw new ResourceNotFoundException("User with such email does not exist");
        }
    }

    public NewAccountDTO createAccount(NewAccountDTO newAccountDTO) {
        Optional<User> user = userRepository.findByEmail(newAccountDTO.getUserEmail());
        if (user.isPresent()) {
            Account account = getAccountFromAccountDTO(newAccountDTO);
            Account savedAccount = accountRepository.save(account);
            addAccountToUserInDB(savedAccount, user.get());

            newAccountDTO.setId(savedAccount.getId());
            return newAccountDTO;
        } else {
            throw new ResourceNotFoundException("Couldn't add account to user. User with this email does not exist");
        }
    }

    public NewAccountDTO updateAccount(NewAccountDTO updatedAccountDTO) {
        Optional<User> user = userRepository.findByEmail(updatedAccountDTO.getUserEmail());
        if (user.isPresent()) {
            Optional<Account> accountToUpdateOptional = accountRepository.findById(updatedAccountDTO.getId());
            if (accountToUpdateOptional.isPresent()) {
                Double balanceDifference = updatedAccountDTO.getStartingBalance() - accountToUpdateOptional.get().getStartingBalance();
                changeCurrentBalanceOfAccountDTO(updatedAccountDTO, balanceDifference);
                Account accountToUpdate = getAccountFromAccountDTO(updatedAccountDTO);
                accountToUpdate.setId(updatedAccountDTO.getId());
                accountRepository.save(accountToUpdate);
                return updatedAccountDTO;
            } else {
                throw new ResourceNotFoundException("Couldn't update this account. Account with given id does not exist");
            }
        } else {
            throw new ResourceNotFoundException("Couldn't update account of user. User with this email does not exist");
        }
    }

    public boolean deleteAccount(Long id, String email) {
        Optional<User> possibleUser = userRepository.findByEmail(email);
        if(possibleUser.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this account. User for this email does not exist");
        }

        Optional<Account> possibleAccountToRemove = possibleUser.get().getAccounts()
                .stream()
                .filter(account -> account.getId().equals(id))
                .findFirst();

        if(possibleAccountToRemove.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this account. User don't have account for this id");
        }

        Account accountToRemove = possibleAccountToRemove.get();
        accountToRemove.setIsEnabled(false);
        accountRepository.save(accountToRemove);
        return true;
    }

    private Account getAccountFromAccountDTO(NewAccountDTO newAccountDTO) {
        Account account = new Account();
        account.setName(newAccountDTO.getName());
        account.setStartingBalance(newAccountDTO.getStartingBalance());
        account.setCurrentBalance(newAccountDTO.getCurrentBalance());
        account.setAccountType(Enum.valueOf(AccountType.class, newAccountDTO.getAccountType().toUpperCase()));
        account.setCurrency(Currency.USD);

        return account;
    }

    private void addAccountToUserInDB(Account savedAccount, User modifiedUser) {
        Set<Account> userAccounts = modifiedUser.getAccounts();
        userAccounts.add(savedAccount);
        modifiedUser.setAccounts(userAccounts);
        userRepository.save(modifiedUser);
    }

    private void changeCurrentBalanceOfAccountDTO(NewAccountDTO accountDTO, Double difference) {
        Double newCurrentBalance = accountDTO.getCurrentBalance() + difference;
        accountDTO.setCurrentBalance(newCurrentBalance);
    }
}
