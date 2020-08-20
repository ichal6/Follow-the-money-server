package com.example.mlkb.service;

import com.example.mlkb.entity.LoginData;
import com.example.mlkb.entity.Profile;
import com.example.mlkb.modelDTO.LoginDataDTO;
import com.example.mlkb.modelDTO.ProfileDTO;
import com.example.mlkb.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity<String> updateProfile(ProfileDTO updateProfile) {
        Optional<Profile> profileOptional = profileRepository.findById(updateProfile.getId());
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            profile.setName(updateProfile.getName());
            Profile updatedProfile = profileRepository.save(profile);
            Optional<Profile> savedProfileOptional = profileRepository.findById(updatedProfile.getId());
            if (savedProfileOptional.isPresent() && profile.equals(savedProfileOptional.get())) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Profile updated successfully!");
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed updating Profile!");
            }
        } else {
            return ResponseEntity.unprocessableEntity().body("Could not update profile. This profile does not exist!");
        }
    }

    public List<ProfileDTO> getAllProfiles() {
        List<Profile> profileList = profileRepository.findAll();
        return profileList.stream()
                .map(x -> new ProfileDTO(x.getId() , x.getName(), x.getDate()))
                .collect(Collectors.toList());
    }

    public Optional<Profile> getProfile(Long id) {
        return profileRepository.findById(id);
    }
}
