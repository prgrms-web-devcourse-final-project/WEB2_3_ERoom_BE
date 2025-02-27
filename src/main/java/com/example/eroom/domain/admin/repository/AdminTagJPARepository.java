package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.SubCategory;
import com.example.eroom.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminTagJPARepository extends JpaRepository<Tag, Long> {
    List<Tag> findBySubCategory(SubCategory subCategory);
}
