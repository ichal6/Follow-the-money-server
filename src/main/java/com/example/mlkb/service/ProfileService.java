package com.example.mlkb.service;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.entity.Profile;
import com.example.mlkb.modelDTO.ProfileDTO;
import com.example.mlkb.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public boolean isValidWithoutId(ProfileDTO profileDTO) {
        return profileDTO != null
                && profileDTO.getName() != null;
    }

    public boolean isValidWithId(ProfileDTO profileDTO) {
        return profileDTO != null
                && profileDTO.getId() != null
                && profileDTO.getName() != null;
    }

    public ResponseEntity<String> createProfile(ProfileDTO profileDTO) {
        Profile profile = new Profile(profileDTO.getName(), profileDTO.getDate());
        Profile saveProfile = profileRepository.save(profile);
        if (profileRepository.findById(saveProfile.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Profile added successfully!");
        } else {
            return ResponseEntity.unprocessableEntity().body("Failed creating Profile!");
        }
    }
}
