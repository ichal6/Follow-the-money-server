package com.example.mlkb.controller;

import com.example.mlkb.entity.User;
import com.example.mlkb.modelDTO.UserDTO;
import com.example.mlkb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST - create new user
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody UserDTO newUser){
        if (userService.isValidWithoutId(newUser)) {
            if(userService.createUser(newUser)){
                return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }

    @GetMapping("/")
    public String greetings(){
        return "Wejście Smoka";
    }

    // GET - get all users
    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> getUsers(){
        List<UserDTO> userDTOList = userService.getAllUsers();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // GET - get user by id
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id){
        Optional<User> userOptional = userService.getUser(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getDate());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else{
            return ResponseEntity.badRequest().body("The user with this id does not exists in the database!");
        }
    }

    // DELETE - delete by id
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id){
        if(userService.deleteUser(id)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
        }else {
            return ResponseEntity.unprocessableEntity().body("Could not delete user!");
        }
    }

    // PUT - update
    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO updateUser){
        if (userService.isValidWithId(updateUser)) {
            if(userService.updateUser(updateUser)){
                return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully!");
            }else{
                ResponseEntity.unprocessableEntity().body("Could not update user. This user does not exist!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }
}
