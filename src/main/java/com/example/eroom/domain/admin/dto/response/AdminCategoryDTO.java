package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.Category;
import com.example.eroom.domain.entity.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryDTO {
    private Long id;
    private String name;
    private List<AdminSubCategoryDTO> subcategories;

    public AdminCategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.subcategories = category.getSubCategories().stream()
                .map(AdminSubCategoryDTO::new)
                .collect(Collectors.toList());
    }
}
