package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Authorities;
import com.mlkb.ftm.entity.AuthorityType;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.repository.UserRepository;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidWithoutId(UserDTO userDTO) {
        return userDTO != null
                && userDTO.getName() != null;
    }

    public boolean isValidNewUser(NewUserDTO userDTO) {
        return userDTO != null
                && userDTO.getName() != null
                && checkEmail(userDTO.getEmail())
                && checkPassword(userDTO.getPassword());
    }

    private boolean checkPassword(String password) {
        //TODO It should be more complicated in production
        return password != null
                && !password.isBlank();
    }

    private boolean checkEmail(String email) {
        //TODO It should be more complicated in production
        return email != null
                && !email.isBlank();
    }

    public Optional<UserDTO> getUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<UserDTO> optionalUserDTO = Optional.empty();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user.getName(), user.getEmail(), user.getDate());
            optionalUserDTO = Optional.of(userDTO);
        }
        return optionalUserDTO;
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

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(x -> new UserDTO(x.getName(), x.getEmail(), x.getDate()))
                .collect(Collectors.toList());
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
