package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSubCategoryJPARepository extends JpaRepository<SubCategory, Long> {
}
