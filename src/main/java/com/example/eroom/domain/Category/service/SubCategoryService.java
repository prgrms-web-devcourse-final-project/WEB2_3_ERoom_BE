package com.example.eroom.domain.Category.service;

import com.example.eroom.domain.Category.dto.response.SubCategoryDTO;
import com.example.eroom.domain.Category.dto.response.TagDTO;
import com.example.eroom.domain.Category.repository.CategoryJpaRepository;
import com.example.eroom.domain.Category.repository.SubCategoryJpaRepository;
import com.example.eroom.domain.Category.repository.TagJpaRepository;
import com.example.eroom.domain.entity.Category;
import com.example.eroom.domain.entity.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final SubCategoryJpaRepository subCategoryJpaRepository;
    private final TagJpaRepository tagJpaRepository;

    public List<SubCategoryDTO> getSubCategories(Long categoryId) {

        Category category = categoryJpaRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 카테고리가 없습니다.: " + categoryId));

        return subCategoryJpaRepository.findByCategory(category)
                .stream()
                .map(SubCategoryDTO::new)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getAllTags(){
        return tagJpaRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Tag::getName,
                        Tag::getCount
                ));
    }
}
