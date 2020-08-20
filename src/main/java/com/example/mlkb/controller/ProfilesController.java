package com.example.mlkb.controller;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.entity.Profile;
import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.modelDTO.ProfileDTO;
import com.example.mlkb.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @GetMapping("/profile")
    public ResponseEntity<List<ProfileDTO>> getProfiles(){
        List<ProfileDTO> profileDTOList = profileService.getAllProfiles();
        return new ResponseEntity<>(profileDTOList, HttpStatus.OK);
    }

    // GET - get profile by id
    @GetMapping("/profile/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable("id") Long id){
        Optional<Profile> profileOptional = profileService.getProfile(id);
        if(profileOptional.isPresent()){
            Profile profile = profileOptional.get();
            ProfileDTO profileDTO = new ProfileDTO(profile.getId(), profile.getName(), profile.getDate());
            return new ResponseEntity<>(profileDTO, HttpStatus.OK);
        } else{
            return ResponseEntity.badRequest().body("The profile with this id does not exists in the database!");
        }
    }

    // DELETE - delete by id
    @DeleteMapping("/profile/{id}")
    public void deleteProfile(@PathVariable("id") String id){
        //loginDataService.deleteLogin(email);
    }

    // PUT - update
    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDTO updateProfile){
        if (profileService.isValidWithId(updateProfile)) {
            return profileService.updateProfile(updateProfile);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your JSON request is invalid.");
    }
}
