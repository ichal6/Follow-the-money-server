package com.example.mlkb.controller;

import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.service.LoginDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginDataController {
    private LoginDataService loginDataService;

    public LoginDataController(LoginDataService loginDataService){
        this.loginDataService = loginDataService;
    }


    // post, tworzy LoginData
    @PostMapping("/login")
    public ResponseEntity<String> createLogin(@RequestBody LoginDataDTO newLogin){
        if (loginDataService.isValid(newLogin)) {
            return loginDataService.createLoginData(newLogin);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }


    // get, zwraca profileID/profile, parametry - login i hasło, sprawdza
    @GetMapping("/login")
    public void getProfile(){

    }
    // delete, usuwa LoginData
    // PUT, modyfikuje LoginData

}
