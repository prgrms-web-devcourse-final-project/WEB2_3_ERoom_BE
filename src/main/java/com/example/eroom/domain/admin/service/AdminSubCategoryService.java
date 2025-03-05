package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.response.AdminSubCategoryDTO;
import com.example.eroom.domain.admin.repository.AdminCategoryJPARepository;
import com.example.eroom.domain.admin.repository.AdminSubCategoryJPARepository;
import com.example.eroom.domain.entity.Category;
import com.example.eroom.domain.entity.SubCategory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminSubCategoryService {
    private final AdminSubCategoryJPARepository adminSubCategoryJPARepository;
    private final AdminCategoryJPARepository adminCategoryJPARepository;

    // [ 서브 카테고리 조회 ]
    public List<AdminSubCategoryDTO> getSubCategories(Long categoryId){
        Category category = adminCategoryJPARepository.findById(categoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 카테고리가 없습니다.: " + categoryId));

        return adminSubCategoryJPARepository.findByCategory(category)
                .stream()
                .map(AdminSubCategoryDTO::new)
                .collect(Collectors.toList());
    }

    // [ 서브 카테고리 생성 ]
    public AdminSubCategoryDTO createSubCategory(Long categoryId, AdminSubCategoryDTO dto) {
        // 1. 카테고리 id로 기존 카테고리 조회
        Category category = adminCategoryJPARepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. : " + categoryId));

        // 2. 서브 카테고리 생성 (빌더 패턴 사용)
        SubCategory subCategory = SubCategory.builder()
                .name(dto.getName())
                .category(category)
                .build();

        // 3. 서브 카테고리 저장
        SubCategory savedSubCategory = adminSubCategoryJPARepository.save(subCategory);

        // 4. 저장된 서브 카테고리 DTO로 변환하여 반환
        return new AdminSubCategoryDTO(savedSubCategory);
    }

    // [ 서브 카테고리 수정 ]
    @Transactional
    public AdminSubCategoryDTO updateSubCategory(Long subcategoryId, AdminSubCategoryDTO dto) {
        // 1. 서브 카테고리 존재 여부 확인
        SubCategory existingSubCategory = adminSubCategoryJPARepository.findById(subcategoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 서브 카테고리가 없습니다.: " + subcategoryId));

        // 2. 새 객체를 생성하지 않고 빌더를 이용한 값 업데이트
        existingSubCategory = existingSubCategory.toBuilder()
                .name(dto.getName() != null ? dto.getName() : existingSubCategory.getName()) // 값이 있으면 변경, 없으면 기존값 유지
                .build();

        return new AdminSubCategoryDTO(existingSubCategory);
    }


    // [ 서브 카테고리 삭제 ]
    public void deleteSubCategory(Long subcategoryId){
        // 1. 서브 카테고리 존재 여부 확인
        SubCategory existingSubCategory = adminSubCategoryJPARepository.findById(subcategoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 서브 카테고리가 없습니다.: " + subcategoryId));

        adminSubCategoryJPARepository.delete(existingSubCategory);
    }
}
