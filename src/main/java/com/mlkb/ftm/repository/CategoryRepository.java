package com.mlkb.ftm.repository;

import com.mlkb.ftm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT CASE WHEN (count(*)) > 0 THEN true ELSE false END FROM Category c " +
            "WHERE c.id = :subcategoryId AND c.parentCategory.id=:categoryId AND c.owner.id=:ownerId")
    boolean existsByIdAndSubcategoryIdAndOwnerId(@Param("categoryId") Long categoryId,
                                                 @Param("subcategoryId") Long subcategoryId,
                                                 @Param("ownerId") Long ownerId);

    @Query("SELECT c FROM Category c WHERE c.owner.id = :ownerId AND c.parentCategory is null")
    Set<Category> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query( "SELECT ca " +
            "FROM User u INNER JOIN u.categories ca WHERE ca.id = ?1 AND u.email = ?2")
    Optional<Category> findByCategoryIdAndUserEmail(Long Id, String email);

}
