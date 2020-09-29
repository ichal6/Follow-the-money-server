package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.Subcategory;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.modelDTO.DashboardDTO;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final UserRepository userRepository;

    public CategoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<CategoryDTO> getCategories(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<CategoryDTO> categories;

            Set<Category> userCategories = user.getCategories();
            categories = getListOfCategories(userCategories);
            return categories;

        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
    }

    private List<CategoryDTO> getListOfCategories(Set<Category> categories){
        return categories.stream()
                .map(category -> new CategoryDTO(category.getId(),
                    category.getName(), category.getGeneralType(),
                        getListOfSubcategories(category.getSubcategories())))
                .collect(Collectors.toList());
    }

    private List<SubcategoryDTO> getListOfSubcategories(Set<Subcategory> subcategories){
        return subcategories.stream()
                .map(subcategory -> new SubcategoryDTO(subcategory.getId(),
                        subcategory.getName(), subcategory.getType()))
                .collect(Collectors.toList());
    }

    public void deleteCategory(String email, Long id) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Set<Category> newCategories = userCategories.stream()
                    .filter(category -> !category.getId().equals(id))
                    .collect(Collectors.toSet());
            user.setCategories(newCategories);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
    }
}
