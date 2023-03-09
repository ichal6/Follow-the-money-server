package com.mlkb.ftm.config;

import com.mlkb.ftm.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import static java.lang.String.format;

@Service
public class PostgresUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PostgresUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetailsDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(UserDetailsDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException(format("Not found user for %s username", username)));
    }


}
