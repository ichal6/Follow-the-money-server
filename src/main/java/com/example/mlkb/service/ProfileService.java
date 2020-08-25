package com.example.mlkb.service;

import com.example.mlkb.entity.User;
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
        User user = new User(profileDTO.getName(), profileDTO.getDate());
        User saveUser = profileRepository.save(user);
        if (profileRepository.findById(saveUser.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Profile added successfully!");
        } else {
            return ResponseEntity.unprocessableEntity().body("Failed creating Profile!");
        }
    }

    @Transactional
    public ResponseEntity<String> updateProfile(ProfileDTO updateProfile) {
        Optional<User> profileOptional = profileRepository.findById(updateProfile.getId());
        if (profileOptional.isPresent()) {
            User user = profileOptional.get();
            user.setName(updateProfile.getName());
            User updatedUser = profileRepository.save(user);
            Optional<User> savedProfileOptional = profileRepository.findById(updatedUser.getId());
            if (savedProfileOptional.isPresent() && user.equals(savedProfileOptional.get())) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Profile updated successfully!");
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed updating Profile!");
            }
        } else {
            return ResponseEntity.unprocessableEntity().body("Could not update profile. This profile does not exist!");
        }
    }

    public List<ProfileDTO> getAllProfiles() {
        List<User> userList = profileRepository.findAll();
        return userList.stream()
                .map(x -> new ProfileDTO(x.getId() , x.getName(), x.getDate()))
                .collect(Collectors.toList());
    }

    public Optional<User> getProfile(Long id) {
        return profileRepository.findById(id);
    }

    public void deleteProfile(Long id) {
        if(profileRepository.findById(id).isPresent()){
            profileRepository.deleteById(id);
        }
    }
}
