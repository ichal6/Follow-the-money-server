package com.example.mlkb.service;

import com.example.mlkb.modelDTO.LoginDataDTO;
import org.springframework.stereotype.Service;

@Service
public class LoginDataService {
    public boolean isValid(LoginDataDTO loginDataDTO){
        return loginDataDTO != null
                && loginDataDTO.getPassword() != null
                && loginDataDTO.getEmail() != null;
    }

}
