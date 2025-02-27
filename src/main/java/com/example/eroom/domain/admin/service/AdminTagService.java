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

        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setSubCategory(subCategory);

        Tag savedTag = adminTagJPARepository.save(tag);
        return savedTag;
    }

    // [ 태그 수정 ]
    @Transactional
    public AdminTagDTO updateTag(Long tagId, AdminTagDTO dto) {
        // 1. 태그 존재 여부 확인
        Tag existingTag = adminTagJPARepository.findById(tagId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 태그가 없습니다.: " + tagId));

        // 2. 값 업데이트 : 이름
        if (dto.getName() != null) {
            existingTag.setName(dto.getName());
        }

        return new AdminTagDTO(existingTag);
    }

    // [ 태그 삭제 ]
    public void deleteTag(Long tagId) {
        // 1. 태그 존재 여부 확인
        Tag existingTag = adminTagJPARepository.findById(tagId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 태그가 없습니다.: " + tagId));

        adminTagJPARepository.delete(existingTag);
    }

}
