package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.AccountDTO;
import com.mlkb.ftm.modelDTO.NewAccountDTO;
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{email}")
    public ResponseEntity<Object> createAccount(@RequestBody NewAccountDTO newAccount) {
        try {
            if (accountService.isValidNewAccount(newAccount)) {
                return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(newAccount));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't add new account. Your JSON is invalid");
            }
        } catch (InputIncorrectException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PutMapping("/api/user")
//    public ResponseEntity<String> updateUser(@RequestBody UserDTO updateUser) {
//        if (userService.isValidWithoutId(updateUser)) {
//            if (userService.updateUser(updateUser)) {
//                return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully!");
//            } else {
//                ResponseEntity.unprocessableEntity().body("Could not update user. This user does not exist!");
//            }
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
//    }
}
