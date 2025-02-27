package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSubCategoryDTO {
    private Long id;
    private String name;
    private List<AdminTagDTO> tags;

    public AdminSubCategoryDTO(SubCategory subCategory) {
        this.id = subCategory.getId();
        this.name = subCategory.getName();
        this.tags = subCategory.getTags().stream()
                .map(AdminTagDTO::new)
                .collect(Collectors.toList());
    }
}
