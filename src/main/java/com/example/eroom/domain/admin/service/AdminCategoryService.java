package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.response.AdminCategoryDTO;
import com.example.eroom.domain.admin.repository.AdminCategoryJPARepository;
import com.example.eroom.domain.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {
    private final AdminCategoryJPARepository adminCategoryJPARepository;

    // [ 전체 카테고리 조회 ]
    public List<AdminCategoryDTO> getCategoryLists() {
        return adminCategoryJPARepository.findAll()
                .stream()
                .map(AdminCategoryDTO::new) // 엔티티 -> DTO 변환
                .collect(Collectors.toList());
    }

    // [ 개별 카테고리 조회 ]
    public AdminCategoryDTO getCategoryById(Long id) {
        return adminCategoryJPARepository.findById(id)
                .map(AdminCategoryDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리가 존재하지 않습니다.: " + id));
    }

    // [ 카테고리 생성 ]
    public Category createCategory(AdminCategoryDTO dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        return adminCategoryJPARepository.save(category);
    }

    // [ 카테고리 수정 ]
    @Transactional
    public AdminCategoryDTO updateCategory(Long categoryId, AdminCategoryDTO dto) {
        // 1. 카테고리 존재 여부 확인
        Category existingCategory = adminCategoryJPARepository.findById(categoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 카테고리가 없습니다.: " + categoryId));

        // 2. 값 업데이트 : 이름
        Category updatedCategory = existingCategory.toBuilder()
                .name(dto.getName() != null ? dto.getName() : existingCategory.getName())
                .build();

        // 저장 및 반환
        Category savedCategory = adminCategoryJPARepository.save(updatedCategory);
        return new AdminCategoryDTO(savedCategory);
    }

    // [ 카테고리 삭제 ]
    public void deleteCategory(Long categoryId) {
        // 1. 카테고리 존재 여부 확인
        Category existingCategory = adminCategoryJPARepository.findById(categoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 카테고리가 없습니다.: " + categoryId));

        // 2. 카테고리 삭제
        adminCategoryJPARepository.delete(existingCategory);
    }
}
