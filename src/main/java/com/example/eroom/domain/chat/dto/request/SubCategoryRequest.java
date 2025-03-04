package com.example.eroom.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SubCategoryRequest {

    private Long subCategoryId;
    private List<Long> tagIds; // 해당 서브 카테고리에서 선택한 태그 id
}
