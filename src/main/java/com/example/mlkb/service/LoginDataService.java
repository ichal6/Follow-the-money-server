package com.example.mlkb.service;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.repository.LoginDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginDataService {
    private LoginDataRepository loginDataRepository;

    public LoginDataService(LoginDataRepository loginDataRepository) {
        this.loginDataRepository = loginDataRepository;
    }

    public boolean isValid(LoginDataDTO loginDataDTO) {
        return loginDataDTO != null
                && loginDataDTO.getPassword() != null
                && loginDataDTO.getEmail() != null;
    }

    public ResponseEntity<String> createLoginData(LoginDataDTO loginDataDTO) {
        if (loginDataRepository.findByEmail(loginDataDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("This email is already present in the database!");
        } else {
            LoginData loginData = new LoginData(loginDataDTO.getEmail(), loginDataDTO.getPassword());
            LoginData saveLoginData = loginDataRepository.save(loginData);
            if (loginDataRepository.findById(saveLoginData.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Login data added successfully!");
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed creating Login Data!");
            }
        }
    }
}
