package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class AccountsController {
    private final AccountService accountService;

    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getAllAccountsFromUser(@PathVariable("email") String email) {
        try {
            List<AccountDTO> accountDTOList = accountService.getAllAccountsFromUser(email);
            return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Object> createAccount(@RequestBody AccountDTO newAccount) {
        if (accountService.isValidAccount(newAccount)) {
            Optional<UserDTO> optionalUserDTO = userService.getUser(newUser.getEmail());
            if (optionalUserDTO.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This email exists in the database!");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }
}
