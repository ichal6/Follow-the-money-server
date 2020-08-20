package com.example.mlkb.service;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.repository.LoginDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoginDataService {
    private final LoginDataRepository loginDataRepository;

    public LoginDataService(LoginDataRepository loginDataRepository) {
        this.loginDataRepository = loginDataRepository;
    }

    public boolean isValidWithoutId(LoginDataDTO loginDataDTO) {
        return loginDataDTO != null
                && loginDataDTO.getPassword() != null
                && loginDataDTO.getEmail() != null;
    }

    public boolean isValidWithId(LoginDataDTO loginDataDTO) {
        return loginDataDTO != null
                && loginDataDTO.getId() != null
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

    public List<LoginDataDTO> getAllLogins(){
        List<LoginData> loginDataList = loginDataRepository.findAll();
        return loginDataList.stream()
                .map(x -> new LoginDataDTO(x.getId() , x.getEmail(), x.getPassword()))
                .collect(Collectors.toList());
    }

    public Optional<LoginData> getLogin(String email){
        return loginDataRepository.findByEmail(email);
    }

    @Transactional
    public void deleteLogin(String email){
        loginDataRepository.deleteByEmail(email);
    }

    @Transactional
    public ResponseEntity<String> updateLogin(LoginDataDTO loginDataDTO){
        if (loginDataRepository.findByEmail(loginDataDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("This email is already present in the database!");
        } else {
            LoginData loginData = new LoginData(loginDataDTO.getId(), loginDataDTO.getEmail(), loginDataDTO.getPassword());
            LoginData saveLoginData = loginDataRepository.save(loginData);
            Optional<LoginData> loginDataOptional = loginDataRepository.findById(saveLoginData.getId());
            if (loginDataOptional.isPresent() && loginData.equals(loginDataOptional.get())) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Login data updated successfully!");
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed updating Login Data!");
            }
        }
    }
}
