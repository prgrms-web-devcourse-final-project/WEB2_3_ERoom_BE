package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.response.AdminTagDTO;
import com.example.eroom.domain.admin.repository.AdminSubCategoryJPARepository;
import com.example.eroom.domain.admin.repository.AdminTagJPARepository;
import com.example.eroom.domain.entity.SubCategory;
import com.example.eroom.domain.entity.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTagService {
    private final AdminTagJPARepository adminTagJPARepository;
    private final AdminSubCategoryJPARepository adminSubCategoryJPARepository;

    // [ 태그 목록 조회 ]
    public List<AdminTagDTO> getTags(Long subCategoryId) {
        SubCategory subCategory = adminSubCategoryJPARepository.findById(subCategoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 서브 카테고리가 없습니다.: " + subCategoryId));

        return adminTagJPARepository.findBySubCategory(subCategory)
                .stream()
                .map(AdminTagDTO::new)
                .collect(Collectors.toList());
    }

    // [ 태그 생성 ]
    public Tag createTag(Long subCategoryId, AdminTagDTO dto) {
        // 서브 카테고리 id 조회
        SubCategory subCategory = adminSubCategoryJPARepository.findById(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. : " + subCategoryId));

        // 빌더 패턴을 사용하여 Tag 객체 생성
        Tag tag = Tag.builder()
                .name(dto.getName())
                .subCategory(subCategory)
                .count(0) // 초기 카운트 값 설정
                .build();

        Tag savedTag = adminTagJPARepository.save(tag);
        return savedTag;
    }

    // [ 태그 수정 ]
    @Transactional
    public AdminTagDTO updateTag(Long tagId, AdminTagDTO dto) {
        // 1. 태그 존재 여부 확인
        Tag existingTag = adminTagJPARepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 태그가 없습니다.: " + tagId));

        // 2. 빌더 패턴을 사용하여 새로운 Tag 객체 생성
        // 업데이트할 값만 변경하고 나머지는 기존 값 유지
        Tag updatedTag = Tag.builder()
                .id(existingTag.getId())
                .name(dto.getName() != null ? dto.getName() : existingTag.getName())
                .subCategory(existingTag.getSubCategory())
                .count(existingTag.getCount())
                .build();

        // 변경된 태그 저장
        adminTagJPARepository.save(updatedTag);

        return new AdminTagDTO(updatedTag);
    }

    // [ 태그 삭제 ]
    public void deleteTag(Long tagId) {
        // 1. 태그 존재 여부 확인
        Tag existingTag = adminTagJPARepository.findById(tagId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 태그가 없습니다.: " + tagId));

        adminTagJPARepository.delete(existingTag);
    }

}
