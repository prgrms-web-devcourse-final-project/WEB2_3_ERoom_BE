package com.example.eroom.domain.Category.service;

import com.example.eroom.domain.Category.dto.response.CategoryDTO;
import com.example.eroom.domain.Category.repository.CategoryJpaRepository;
import com.example.eroom.domain.admin.dto.response.AdminCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    public List<CategoryDTO> getCategoryLists() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(CategoryDTO::new) // 엔티티 -> DTO 변환
                .collect(Collectors.toList());
    }
}
