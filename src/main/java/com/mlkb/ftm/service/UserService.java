package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Authorities;
import com.mlkb.ftm.entity.AuthorityType;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;

    public UserService(UserRepository userRepository, InputValidator inputValidator) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
    }

    public boolean isValidWithoutId(UserDTO userDTO) throws InputIncorrectException {
        return userDTO != null
                && inputValidator.checkName(userDTO.getName());
    }

    public boolean isValidNewUser(NewUserDTO userDTO) throws InputIncorrectException {
        return userDTO != null
                && inputValidator.checkName(userDTO.getName())
                && inputValidator.checkEmail(userDTO.getEmail())
                && inputValidator.checkPassword(userDTO.getPassword());
    }

    public boolean isUserInDB(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent();
    }

    public UserDTO getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDTO(user.getName(), user.getEmail(), user.getDate());
        } else {
            throw new ResourceNotFoundException("Couldn't get this user. User with this email does not exist");
        }
    }

    public UserDTO createUser(NewUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAuthorities(Collections.singleton(new Authorities(AuthorityType.ROLE_USER)));
        user.setEnabled(1);
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        return new UserDTO(user.getName(), user.getEmail(), user.getDate());
    }




    // NOT CORRECT - TO UPDATE:

    @Transactional
    public boolean updateUser(UserDTO updateUser) {
        Optional<User> userOptional = userRepository.findByEmail(updateUser.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(updateUser.getName());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUser(Long id) {
        System.out.println(userRepository.findById(id));
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
