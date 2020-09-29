package com.mlkb.ftm.controller;

import com.mlkb.ftm.modelDTO.CategoryDTO;
import com.mlkb.ftm.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getCategories(@PathVariable String email){
        try {
            List<CategoryDTO> categoriesDTO = categoryService.getCategories(email);
            return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{email}/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable String email, @PathVariable Long id){
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
        try {
            categoryService.deleteSubcategory(email, idCat, idSub);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
