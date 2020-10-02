package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.GeneralType;
import com.mlkb.ftm.entity.Subcategory;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;

    public CategoryService(UserRepository userRepository, InputValidator inputValidator) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
    }

    public List<CategoryDTO> getCategoriesForExpense(String email){
        return getCategories(email).stream()
                .filter(categoryDTO -> categoryDTO.getType().equals(GeneralType.EXPENSE))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoriesForIncome(String email){
        return getCategories(email).stream()
                .filter(categoryDTO -> categoryDTO.getType().equals(GeneralType.INCOME))
                .collect(Collectors.toList());
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

    public void deleteSubcategory(String email, Long catId, Long subId) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Set<Subcategory> newCategories = userCategories.stream()
                    .filter(category -> category.getId().equals(catId))
                    .findFirst().get()
                    .getSubcategories().stream()
                    .filter(subcategory -> !subcategory.getId().equals(subId))
                    .collect(Collectors.toSet());

            userCategories.stream()
                    .filter(category -> category.getId().equals(catId))
                    .findFirst().get().setSubcategories(newCategories);
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
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
            categoryToEdit.setGeneralType(categoryDTO.getType());
            userCategories.add(categoryToEdit);
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
    }

    public void updateSubcategory(String email, Long catId, Long subId, String newName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Subcategory subcategoryToEdit = userCategories.stream()
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
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
    }

    public void addCategory(String email, CategoryDTO newCategory) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            userCategories.add(new Category(null, newCategory.getName(), newCategory.getType(), new HashSet<>()));
            user.setCategories(userCategories);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
    }

    public void addSubcategory(String email, Long id, SubcategoryDTO newSubcategory) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Set<Category> userCategories = user.getCategories();
            Category categoryToEdit = userCategories.stream()
                    .filter(category -> category.getId().equals(id))
                    .findFirst().get();
            categoryToEdit.getSubcategories()
                    .add(new Subcategory(null, newSubcategory.getName(), newSubcategory.getType()));
            userCategories.add(categoryToEdit);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Couldn't find a categories for user with give email");
        }
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
