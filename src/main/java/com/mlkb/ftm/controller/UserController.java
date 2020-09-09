package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // GET - get user by email
    @GetMapping("/{email}")
    public ResponseEntity<Object> getUser(@PathVariable("email") String email) {
        Optional<UserDTO> optionalUserDTO = userService.getUser(email);
        if (optionalUserDTO.isPresent()) {
            return new ResponseEntity<>(optionalUserDTO.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("The user with this id does not exists in the database!");
        }
    }





    // NOT CORRECT - TO UPDATE:

    // POST - create new user
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserDTO newUser) {
        if (userService.isValidWithoutId(newUser)) {
            if (userService.createUser(newUser)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }

    // GET - get all users
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> userDTOList = userService.getAllUsers();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    // DELETE - delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
        } else {
            return ResponseEntity.unprocessableEntity().body("Could not delete user!");
        }
    }

    // PUT - update
    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody UserDTO updateUser) {
        if (userService.isValidWithoutId(updateUser)) {
            if (userService.updateUser(updateUser)) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully!");
            } else {
                ResponseEntity.unprocessableEntity().body("Could not update user. This user does not exist!");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }
}
