package com.mlkb.ftm.controller;

import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.modelDTO.SubcategoryDTO;
import com.mlkb.ftm.service.CategoryService;
import com.mlkb.ftm.validation.AccessValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final AccessValidator accessValidator;

    public CategoryController(CategoryService categoryService, AccessValidator accessValidator) {
        this.categoryService = categoryService;
        this.accessValidator = accessValidator;
    }

    @GetMapping(value = {"/{email}", "/expense/{email}", "/income/{email}"})
    public ResponseEntity<Object> getCategories(@PathVariable String email) {
        accessValidator.checkPermit(email);
        List<CategoryDTO> categoriesDTO = categoryService.getCategories(email);
        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable String email, @PathVariable Long id) {
        accessValidator.checkPermit(email);
        categoryService.deleteCategory(email, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/{idCat}/{idSub}")
    public ResponseEntity<Object> deleteSubcategory(@PathVariable String email, @PathVariable Long idCat,
                                                 @PathVariable Long idSub) {
        accessValidator.checkPermit(email);
        categoryService.deleteSubcategory(email, idCat, idSub);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PutMapping("/{email}/{id}")
    public ResponseEntity<Object> updateCategory(@PathVariable String email, @PathVariable Long id,
                                                 @RequestBody CategoryDTO categoryToEdit) throws InputIncorrectException {
        accessValidator.checkPermit(email);
        categoryService.isValidNewCategory(categoryToEdit);
        categoryService.updateCategory(email, id, categoryToEdit);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{email}/{idCat}/{idSub}")
    public ResponseEntity<Object> updateSubcategory(@PathVariable String email, @PathVariable Long idCat,
                                                    @PathVariable Long idSub,
                                                    @RequestBody String newName) {
        accessValidator.checkPermit(email);
        categoryService.updateSubcategory(email, idCat, idSub, newName);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/{email}")
    public ResponseEntity<Object> addCategory(@PathVariable String email, @RequestBody CategoryDTO newCategory) throws InputIncorrectException {
        accessValidator.checkPermit(email);
        categoryService.isValidNewCategory(newCategory);
        categoryService.addCategory(email, newCategory);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/{email}/{id}")
    public ResponseEntity<Object> addSubcategory(@PathVariable String email, @PathVariable Long id,
                                              @RequestBody SubcategoryDTO newSubcategory) throws InputIncorrectException {
        accessValidator.checkPermit(email);
        categoryService.addSubcategory(email, id, newSubcategory);
        categoryService.isValidNewSubcategory(newSubcategory);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
