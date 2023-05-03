package com.mlkb.ftm.service;

import com.mlkb.ftm.common.AcceptanceTest;
import com.mlkb.ftm.repository.CategoryRepository;
import com.mlkb.ftm.repository.UserRepository;
import com.mlkb.ftm.validation.InputValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.sql.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    void delete_select_subcategory_in_database() throws SQLException{
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

}
