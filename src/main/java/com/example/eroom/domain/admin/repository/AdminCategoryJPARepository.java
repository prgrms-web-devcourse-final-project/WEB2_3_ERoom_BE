package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCategoryJPARepository extends JpaRepository<Category, Long> {
}
