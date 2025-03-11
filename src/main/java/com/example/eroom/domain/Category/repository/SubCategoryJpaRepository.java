package com.example.eroom.domain.Category.repository;

import com.example.eroom.domain.entity.Category;
import com.example.eroom.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryJpaRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategory(Category category);
}
