package com.example.eroom.domain.Category.dto.response;

import com.example.eroom.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;
    private List<SubCategoryDTO> subcategories;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.subcategories = category.getSubCategories().stream()
                .map(SubCategoryDTO::new)
                .collect(Collectors.toList());
    }
}
