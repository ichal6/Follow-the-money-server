package com.example.mlkb.controller;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.service.LoginDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LoginDataController {
    private LoginDataService loginDataService;

    public LoginDataController(LoginDataService loginDataService){
        this.loginDataService = loginDataService;
    }

    // POST, tworzy entity LoginData
    @PostMapping("/login")
    public ResponseEntity<String> createLogin(@RequestBody LoginDataDTO newLogin){
        if (loginDataService.isValidWithoutId(newLogin)) {
            return loginDataService.createLoginData(newLogin);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }

    // GET, zwraca wszystkie entities LoginData
    @GetMapping("/login")
    public ResponseEntity<List<LoginDataDTO>> getLogins(){
        List<LoginDataDTO> loginDataDTOList = loginDataService.getAllLogins();
        return new ResponseEntity<>(loginDataDTOList, HttpStatus.OK);
    }

    // GET, zwraca entity LoginData na podstawie emaila
    @GetMapping("/login/{email}")
    public ResponseEntity<Object> getLogin(@PathVariable("email") String email){
        Optional<LoginData> loginDataOptional = loginDataService.getLogin(email);
        if(loginDataOptional.isPresent()){
            LoginData loginData = loginDataOptional.get();
            LoginDataDTO loginDataDTO = new LoginDataDTO(loginData.getId(), loginData.getEmail(), loginData.getPassword());
            return new ResponseEntity<>(loginDataDTO, HttpStatus.OK);
        } else{
            return ResponseEntity.badRequest().body("This email does not exists in the database!");
        }
    }

    // DELETE, usuwa LoginData na podstawie emaila
    @DeleteMapping("/login/{email}")
    public void deleteLogin(@PathVariable("email") String email){
        loginDataService.deleteLogin(email);
    }

    // PUT, modyfikuje entity LoginData
    @PutMapping("/login")
    public ResponseEntity<String> updateLogin(@RequestBody LoginDataDTO updateLogin){
        if (loginDataService.isValidWithId(updateLogin)) {
            return loginDataService.updateLogin(updateLogin);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }
}
