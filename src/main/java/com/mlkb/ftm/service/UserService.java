package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Authorities;
import com.mlkb.ftm.entity.AuthorityType;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.repository.AuthorityRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, InputValidator inputValidator, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
        this.authorityRepository = authorityRepository;
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

    public Long getUserId(String email) {
        return userRepository.getUserId(email);
    }

    public UserDTO createUser(NewUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAuthorities(Collections.singleton(getAuthorities(AuthorityType.ROLE_USER)));
        user.setEnabled(1);
        user.setPassword(userDTO.getPassword());
        userRepository.save(user);
        return new UserDTO(user.getName(), user.getEmail(), user.getDate());
    }

    private Authorities getAuthorities(AuthorityType authorityType){
        Optional<Authorities> possibleAuthorities = authorityRepository.findByName(authorityType);
        if (possibleAuthorities.isPresent()){
            return possibleAuthorities.get();
        } else{
            throw new ResourceNotFoundException("There is no ROLE USER authority");
        }
    }




    // NOT CORRECT - TO UPDATE:

//    @Transactional
//    public boolean updateUser(UserDTO updateUser) {
//        Optional<User> userOptional = userRepository.findByEmail(updateUser.getEmail());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setName(updateUser.getName());
//            userRepository.save(user);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean deleteUser(Long id) {
//        System.out.println(userRepository.findById(id));
//        if (userRepository.findById(id).isPresent()) {
//            userRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }
}
