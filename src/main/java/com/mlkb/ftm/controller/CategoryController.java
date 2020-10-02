package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;
import com.mlkb.ftm.service.CategoryService;
import com.mlkb.ftm.validation.AccessValidator;
import com.mlkb.ftm.validation.InputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final AccessValidator accessValidator;
    private final InputValidator inputValidator;

    public CategoryController(CategoryService categoryService, AccessValidator accessValidator,
                              InputValidator inputValidator){
        this.categoryService = categoryService;
        this.accessValidator = accessValidator;
        this.inputValidator = inputValidator;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getCategories(@PathVariable String email){
        accessValidator.checkPermit(email);
        try {
            List<CategoryDTO> categoriesDTO = categoryService.getCategories(email);
            return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/expense/{email}")
    public ResponseEntity<Object> getCategoriesForExpense(@PathVariable String email){
        accessValidator.checkPermit(email);
        try {
            List<CategoryDTO> categoriesDTO = categoryService.getCategoriesForExpense(email);
            return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/income/{email}")
    public ResponseEntity<Object> getCategoriesForIncome(@PathVariable String email){
        accessValidator.checkPermit(email);
        try {
            List<CategoryDTO> categoriesDTO = categoryService.getCategoriesForIncome(email);
            return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable String email, @PathVariable Long id){
        accessValidator.checkPermit(email);
        try {
            categoryService.deleteCategory(email, id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{email}/{idCat}/{idSub}")
    public ResponseEntity<Object> deleteCategory(@PathVariable String email, @PathVariable Long idCat,
                                                 @PathVariable Long idSub){
        accessValidator.checkPermit(email);
        try {
            categoryService.deleteSubcategory(email, idCat, idSub);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{email}/{id}")
    public ResponseEntity<Object> UpdateCategory(@PathVariable String email, @PathVariable Long id,
                                                 @RequestBody CategoryDTO categoryToEdit){
        accessValidator.checkPermit(email);
        try {
            categoryService.isValidNewCategory(categoryToEdit);
            categoryService.updateCategory(email, id, categoryToEdit);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (IllegalArgumentException | InputIncorrectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{email}/{idCat}/{idSub}")
    public ResponseEntity<Object> UpdateSubcategory(@PathVariable String email, @PathVariable Long idCat,
                                                 @PathVariable Long idSub,
                                                 @RequestBody String newName){
        accessValidator.checkPermit(email);
        try {
            categoryService.updateSubcategory(email, idCat, idSub, newName);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{email}")
    public ResponseEntity<Object> AddCategory(@PathVariable String email, @RequestBody CategoryDTO newCategory){
        accessValidator.checkPermit(email);
        try {
            categoryService.isValidNewCategory(newCategory);
            categoryService.addCategory(email, newCategory);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (IllegalArgumentException | InputIncorrectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{email}/{id}")
    public ResponseEntity<Object> AddCategory(@PathVariable String email, @PathVariable Long id,
                                              @RequestBody SubcategoryDTO newSubcategory){
        accessValidator.checkPermit(email);
        try {
            categoryService.addSubcategory(email, id, newSubcategory);
            categoryService.isValidNewSubcategory(newSubcategory);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (IllegalArgumentException | InputIncorrectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
