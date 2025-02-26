package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.ColorInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectCreateRequestDTO {

    private String name;
    private String description;
    private Long categoryId; // 카테고리 ID
    private List<SubCategoryRequest> subCategories; // 여러 서브 카테고리를 담을 수 있도록 변경
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Long> invitedMemberIds;
    private ColorInfo colors;
}
