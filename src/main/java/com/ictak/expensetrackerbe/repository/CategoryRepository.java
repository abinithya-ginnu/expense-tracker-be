package com.ictak.expensetrackerbe.repository;

import com.ictak.expensetrackerbe.dbmodels.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    @Query(value = "SELECT * FROM CATEGORIES",nativeQuery = true)
    List<CategoryEntity> getCategories();

    @Query(value = "SELECT id FROM CATEGORIES WHERE name like %?1%",nativeQuery = true)
    Integer getCategoryId(String name);

}
