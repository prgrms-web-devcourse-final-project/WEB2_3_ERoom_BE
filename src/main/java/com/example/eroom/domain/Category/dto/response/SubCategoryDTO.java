package com.example.eroom.domain.Category.dto.response;

import com.example.eroom.domain.admin.dto.response.AdminTagDTO;
import com.example.eroom.domain.entity.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {

    private Long id;
    private String name;
    private List<AdminTagDTO> tags;

    public SubCategoryDTO(SubCategory subCategory) {
        this.id = subCategory.getId();
        this.name = subCategory.getName();
        this.tags = subCategory.getTags() != null ? subCategory.getTags().stream()
                .map(AdminTagDTO::new)
                .collect(Collectors.toList()) : new ArrayList<>();
    }
}
