package com.example.eroom.domain.Category.service;

import com.example.eroom.domain.Category.dto.response.TagDTO;
import com.example.eroom.domain.Category.repository.SubCategoryJpaRepository;
import com.example.eroom.domain.Category.repository.TagJpaRepository;
import com.example.eroom.domain.entity.SubCategory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final TagJpaRepository tagJpaRepository;

    public List<TagDTO> getTags(Long subCategoryId) {
        SubCategory subCategory = subCategoryJpaRepository.findById(subCategoryId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 서브 카테고리가 없습니다.: " + subCategoryId));

        return tagJpaRepository.findBySubCategory(subCategory)
                .stream()
                .map(TagDTO::new)
                .collect(Collectors.toList());
    }
}
