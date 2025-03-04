package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectUpdateRequestDTO {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    @Size(max = 50, message = "프로젝트 이름은 최대 50자까지 가능합니다.")
    private String name;

    //@NotNull(message = "카테고리는 필수 선택 사항입니다.")
    private Long categoryId;

    //@NotEmpty(message = "최소 하나 이상의 서브 카테고리를 선택해야 합니다.")
    private List<SubCategoryRequest> subCategories;

    @FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
    private LocalDateTime startDate;

    @Future(message = "종료 날짜는 미래여야 합니다.")
    private LocalDateTime endDate;

    @NotNull(message = "프로젝트의 상태를 선택해 주세요.")
    private ProjectStatus status;

    // 기존 멤버 리스트를 완전히 교체할 경우 -> 아마 이것만 쓰게 될 예정
    private List<Long> memberIds;

    // 멤버 추가/삭제
    private List<Long> memberIdsToAdd;
    private List<Long> memberIdsToRemove;
}
