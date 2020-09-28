package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.AccountService;
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

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getAllAccountsFromUser(@PathVariable("email") String email) {
        try {
            List<AccountDTO> accountDTOList = accountService.getAllAccountsFromUser(email);
            return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Object> createAccount(@RequestBody NewAccountDTO newAccount) {
        try {
            if (accountService.isValidNewAccount(newAccount)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(newAccount));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't add new account. Your JSON is invalid");
            }
        } catch (InputIncorrectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity<Object> updateAccount(@RequestBody NewAccountDTO updatedAccount) {
        try {
            if (accountService.isValidNewAccount(updatedAccount)) {
                return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccount(updatedAccount));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't add new account. Your JSON is invalid");
            }
        } catch (InputIncorrectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException httpMessageNotReadableException) {
        String message = "Couldn't process your request. Your JSON is the wrong format";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
