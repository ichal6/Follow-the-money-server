package com.example.mlkb.service;

import com.example.mlkb.entity.User;
import com.example.mlkb.modelDTO.UserDTO;
import com.example.mlkb.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public boolean isValidWithId(UserDTO userDTO) {
        return userDTO != null
                && userDTO.getId() != null
                && userDTO.getName() != null;
    }

    public ResponseEntity<String> createUser(UserDTO userDTO) {
        User user = new User(userDTO.getName(), userDTO.getDate());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
    }

    @Transactional
    public ResponseEntity<String> updateUser(UserDTO updateUser) {
        Optional<User> userOptional = userRepository.findById(updateUser.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(updateUser.getName());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully!");
        } else {
            return ResponseEntity.unprocessableEntity().body("Could not update user. This user does not exist!");
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(x -> new UserDTO(x.getId() , x.getName(), x.getDate()))
                .collect(Collectors.toList());
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        }
    }
}
