package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.ColorInfo;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectCreateRequestDTO {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    @Size(max = 50, message = "프로젝트 이름은 최대 50자까지 가능합니다.")
    private String name;

    @Size(max = 200, message = "설명은 최대 200자까지 가능합니다.")
    private String description;

    @NotNull(message = "카테고리는 필수 선택 사항입니다.")
    private Long categoryId;

    @NotEmpty(message = "최소 하나 이상의 서브 카테고리를 선택해야 합니다.")
    private List<SubCategoryRequest> subCategories;

    @FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
    private LocalDateTime startDate;

    @Future(message = "종료 날짜는 미래여야 합니다.")
    private LocalDateTime endDate;

    private List<Long> invitedMemberIds;

    private ColorInfo colors;
}
