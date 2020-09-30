package com.mlkb.ftm.validation;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AccessValidator {

    public boolean checkPermit(String email) throws IllegalAccessException {
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(emailFromToken == null){
            throw new UsernameNotFoundException("You have to log in before access");
        }
        if(!email.equals(emailFromToken)){
            throw new IllegalAccessException("Access denied");
        }
        return true;
    }
}
