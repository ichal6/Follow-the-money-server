package com.mlkb.ftm.validation;

import com.mlkb.ftm.exception.IllegalAccessRuntimeException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AccessValidator {

    public boolean checkPermit(String email){
        String emailFromToken = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(emailFromToken == null){
            throw new UsernameNotFoundException("You have to log in before access");
        }
        if(!email.equals(emailFromToken)){
            throw new IllegalAccessRuntimeException("Access denied");
        }
        return true;
    }
}
