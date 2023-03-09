package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.NewUserDTO;
import com.mlkb.ftm.modelDTO.UserDTO;
import com.mlkb.ftm.service.UserService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final AccessValidator accessValidator;

    public UserController(UserService userService, AccessValidator accessValidator) {
        this.userService = userService;
        this.accessValidator = accessValidator;
    }

    @GetMapping("/api/user/{email}")
    public ResponseEntity<Object> getUser(@PathVariable("email") String email) {
        accessValidator.checkPermit(email);
        UserDTO userDTO = userService.getUser(email);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@RequestBody NewUserDTO newUser) {
        try {
            userService.isValidNewUser(newUser);
            boolean isUserInDB = userService.isUserInDB(newUser.getEmail());
            if (isUserInDB) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This email exists in the database!");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(newUser));
            }
        } catch (InputIncorrectException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // NOT CORRECT - TO UPDATE:

    // DELETE - delete by id
//    @DeleteMapping("/api/user/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
//        if (userService.deleteUser(id)) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
//        } else {
//            return ResponseEntity.unprocessableEntity().body("Could not delete user!");
//        }
//    }
//
//    // PUT - update
////    @PutMapping("/api/user")
//    public ResponseEntity<String> updateUser(@RequestBody UserDTO updateUser) {
//        try {
//            if (userService.isValidWithoutId(updateUser)) {
//                if (userService.updateUser(updateUser)) {
//                    return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully!");
//                } else {
//                    ResponseEntity.unprocessableEntity().body("Could not update user. This user does not exist!");
//                }
//            }
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
//        } catch (InputIncorrectException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
}
