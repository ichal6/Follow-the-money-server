package com.mlkb.ftm.service;

import com.mlkb.ftm.common.AcceptanceTest;
import com.mlkb.ftm.entity.Category;
import com.mlkb.ftm.entity.User;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.fixture.CategoryDTOFixture;
import com.mlkb.ftm.repository.CategoryRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/category.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryServiceTestIT extends AcceptanceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InputValidator inputValidator;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void delete_select_subcategory_in_database() throws SQLException {
        // given
        Long subcategoryIdToRemove = 8L;
        Long categoryId = 3L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";
        //when
        categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT is_enabled FROM category WHERE id = %d", subcategoryIdToRemove));

                // Check if the data is removed
                assertThat(rs.next()).isTrue();
                assertThat(rs.getBoolean(1)).isFalse();
            }
        }
    }

    @Test
    void try_delete_select_subcategory_in_database_for_wrong_user_email() {
        // given
        Long subcategoryIdToRemove = 8L;
        Long categoryId = 3L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "no_exists";
        //when
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove));
        // then
        assertEquals(String.format("Couldn't find a user with email: %s", email), thrown.getMessage());
    }

    @Test
    void try_delete_select_subcategory_in_database_for_wrong_category_id() {
        // given
        Long subcategoryIdToRemove = 8L;
        Long categoryId = 9999L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";
        //when
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove));
        // then
        assertEquals(
                String.format("Couldn't find a subcategory for given category id = %d and subcategory id = %d",
                categoryId, subcategoryIdToRemove),
                thrown.getMessage()
        );
    }

    @Test
    void try_delete_select_subcategory_in_database_for_wrong_subcategory_id() {
        // given
        Long subcategoryIdToRemove = 88888L;
        Long categoryId = 3L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";
        //when
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove));
        // then
        assertEquals(
                String.format("Couldn't find a subcategory for given category id = %d and subcategory id = %d",
                        categoryId, subcategoryIdToRemove),
                thrown.getMessage()
        );
    }

    @Test
    void try_delete_select_subcategory_in_database_for_wrong_category_id_and_subcategory_id() {
        // given
        Long subcategoryIdToRemove = 88888L;
        Long categoryId = 33333L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";
        //when
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove));
        // then
        assertEquals(
                String.format("Couldn't find a subcategory for given category id = %d and subcategory id = %d",
                        categoryId, subcategoryIdToRemove),
                thrown.getMessage()
        );
    }

    @Test
    void try_delete_select_subcategory_in_database_for_another_user_email() {
        // given
        Long subcategoryIdToRemove = 8L;
        Long categoryId = 3L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "jan.kowalski@gmail.com";
        //when
        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove));
        // then
        assertEquals(
                String.format("Couldn't find a subcategory for given category id = %d and subcategory id = %d",
                        categoryId, subcategoryIdToRemove),
                thrown.getMessage()
        );
    }

    @Test
    void delete_select_subcategory_twice_in_database() throws SQLException {
        // given
        Long subcategoryIdToRemove = 8L;
        Long categoryId = 3L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";
        //when
        categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove);
        categoryService.deleteSubcategory(email, categoryId, subcategoryIdToRemove);
        // then
        // Connect to the database
        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT is_enabled FROM category WHERE id = %d", subcategoryIdToRemove));

                // Check if the data is removed
                assertThat(rs.next()).isTrue();
                assertThat(rs.getBoolean(1)).isFalse();
            }
        }
    }

    @Test
    void add_subcategory_to_database() throws SQLException {
        // given
        Long categoryId = 5L;
        CategoryService categoryService = new CategoryService(this.userRepository, this.categoryRepository, this.inputValidator);
        String email = "user@user.pl";

        var subcategoryDto = CategoryDTOFixture.carSubcategory();

        //when
        categoryService.addSubcategory(email, categoryId, subcategoryDto);

        // then
        Optional<Category> category = categoryRepository.findById(categoryId);
        category.ifPresentOrElse(c -> assertEquals(1, c.getSubcategories().size()), Assertions::fail);

        try (Connection conn = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword())) {
            // Create a statement to query the database
            try (Statement stmt = conn.createStatement()) {
                // Query the database for the data
                ResultSet rs = stmt.executeQuery(String.format("SELECT user_id FROM category WHERE category_id = %d", categoryId));

                // Check if the user_id is set
                assertThat(rs.next()).isTrue();
                assertThat(rs.getLong(1)).isNotNull();
            }
        }
    }
}
