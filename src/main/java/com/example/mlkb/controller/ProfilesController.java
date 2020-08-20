package com.example.mlkb.controller;

import com.example.mlkb.modelDTO.ProfileDTO;
import com.example.mlkb.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfilesController {
    private final ProfileService profileService;

    public ProfilesController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // POST - create new profile
    @PostMapping("/profile")
    public ResponseEntity<String> createProfile(@RequestBody ProfileDTO newProfile){
        if (profileService.isValidWithoutId(newProfile)) {
            return profileService.createProfile(newProfile);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }

    // GET - get all profiles
    // GET - get profile by id
    // DELETE - delete by id
    // PUT - update
}
