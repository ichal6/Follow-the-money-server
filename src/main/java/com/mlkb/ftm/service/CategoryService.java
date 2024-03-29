package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;
import com.mlkb.ftm.repository.CategoryRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final InputValidator inputValidator;

    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository, InputValidator inputValidator) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.inputValidator = inputValidator;
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
            throw new ResourceNotFoundException("Couldn't find a user with given email");
        }
    }

    private List<CategoryDTO> getListOfCategories(Set<Category> categories){
        return categories.stream()
                .filter(c -> c.getParentCategory() == null)
                .map(category -> new CategoryDTO(category.getId(),
                    category.getName(),
                        getListOfSubcategories(category.getSubcategories())))
                .collect(Collectors.toList());
    }

    private List<SubcategoryDTO> getListOfSubcategories(Set<Category> subcategories){
        return subcategories.stream()
                .filter(Category::getIsEnabled)
                .map(subcategory -> new SubcategoryDTO(subcategory.getId(),
                        subcategory.getName()))
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
            throw new ResourceNotFoundException("Couldn't find a user with given email");
        }
    }

    public void deleteSubcategory(String email, Long catId, Long subId) {
        var userId = userRepository.getUserId(email);

        if (userId == null)
            throw new ResourceNotFoundException(String.format("Couldn't find a user with email: %s", email));

        boolean isSubcategory = categoryRepository.existsByIdAndSubcategoryIdAndOwnerId(catId, subId, userId);
        if (!isSubcategory) {
            throw new ResourceNotFoundException(
                    String.format("Couldn't find a subcategory for given category id = %s and subcategory id = %s",
                            catId, subId)
            );
        }
        Optional<Category> subcategoryToDisable = categoryRepository.findById(subId);

        subcategoryToDisable.ifPresent(sub -> {
            sub.setIsEnabled(false);
            categoryRepository.save(sub);
        });
    }

    public void updateCategory(String email, Long id, CategoryDTO categoryDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Category categoryToEdit = userCategories.stream()
                    .filter(category -> category.getId().equals(id))
                    .findFirst().get();
            categoryToEdit.setName(categoryDTO.getName());
            userCategories.add(categoryToEdit);
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Couldn't find a user with given email");
        }
    }

    public void updateSubcategory(String email, Long catId, Long subId, String newName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Category subcategoryToEdit = userCategories.stream()
                    .filter(category -> category.getId().equals(catId))
                    .findFirst().get()
                    .getSubcategories().stream()
                    .filter(subcategory -> subcategory.getId().equals(subId))
                    .findFirst().get();

            subcategoryToEdit.setName(newName);

            userCategories.stream()
                    .filter(category -> category.getId().equals(catId))
                    .findFirst().get().getSubcategories().add(subcategoryToEdit);
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Couldn't find a user with give email");
        }
    }

    public void addCategory(String email, CategoryDTO newCategory) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            userCategories.add(new Category(newCategory.getName()));
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Couldn't find user with given email");
        }
    }

    public void addSubcategory(String email, Long id, SubcategoryDTO newSubcategory) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Couldn't find user for email = %s", email))
        );
        Optional<Category> possibleMainCategory = this.categoryRepository.findByCategoryIdAndUserEmail(id, email);
        Category category = possibleMainCategory.orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Couldn't find category for id = %d and email = %s", id, email))
                );
        category.getSubcategories().add(new Category(newSubcategory.getName(), user));
        categoryRepository.save(category);
    }

    public boolean isValidNewCategory(CategoryDTO categoryDTO) throws InputIncorrectException {
        return categoryDTO != null
                && inputValidator.checkName(categoryDTO.getName());
    }

    public boolean isValidNewSubcategory(SubcategoryDTO subcategoryDTO) throws InputIncorrectException {
        return subcategoryDTO != null
                && inputValidator.checkName(subcategoryDTO.getName());
    }
}
