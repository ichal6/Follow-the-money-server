package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.AccountService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    private final AccessValidator accessValidator;

    public AccountController(AccountService accountService, AccessValidator accessValidator) {
        this.accountService = accountService;
        this.accessValidator = accessValidator;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getAllAccountsFromUser(@PathVariable("email") String email) {
        accessValidator.checkPermit(email);
        List<AccountDTO> accountDTOList = accountService.getAllAccountsFromUser(email);
        return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Object> createAccount(@RequestBody NewAccountDTO newAccount) throws InputIncorrectException {
        accountService.isValidNewAccount(newAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(newAccount));
    }

    @PutMapping()
    public ResponseEntity<Object> updateAccount(@RequestBody NewAccountDTO updatedAccount) throws InputIncorrectException {
        accountService.isValidNewAccount(updatedAccount);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(updatedAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
