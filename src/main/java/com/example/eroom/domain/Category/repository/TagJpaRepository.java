package com.example.eroom.domain.Category.repository;

import com.example.eroom.domain.entity.SubCategory;
import com.example.eroom.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    List<Tag> findBySubCategory(SubCategory subCategory);
}
