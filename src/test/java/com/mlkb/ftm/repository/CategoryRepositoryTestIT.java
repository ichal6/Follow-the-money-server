package com.mlkb.ftm.repository;

import com.mlkb.ftm.common.IntegrationTest;
import com.mlkb.ftm.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Sql({
        "classpath:/sql/user.sql",
        "classpath:/sql/category.sql"
})
@Sql(value = "classpath:/sql/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryRepositoryTestIT extends IntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void should_get_all_category_without_subcategories_for_user_id() {
        // given
        Long userId = 1L;
        // when
        Set<Category> allCategories = this.categoryRepository.findAllByOwnerId(userId);
        //then
        assertThat(allCategories)
                .hasSize(5);

        var unexpectedSubcategory = getUnexpectedSubcategory(allCategories);
        assertThat(unexpectedSubcategory).isEmpty();

        var possibleCategoryForOtherUser = getUnexpectedCategoryFromOtherUser(allCategories, userId);
        assertThat(possibleCategoryForOtherUser).isEmpty();
    }

    private Optional<Category> getUnexpectedSubcategory(Set<Category> categories) {
        return categories.stream()
                .filter(c -> c.getParentCategory() != null)
                .findAny();
    }

    private Optional<Category> getUnexpectedCategoryFromOtherUser(Set<Category> categories, Long userId) {
        return categories.stream()
                .filter(c -> !Objects.equals(c.getOwner().getId(), userId))
                .findAny();
    }
}
