package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectUpdateRequestDTO {
    private String name;
    private String tag1;
    private String tag2;
    private String tag3;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;

    // 기존 멤버 리스트를 완전히 교체할 경우 -> 아마 이것만 쓰게 될 예정
    private List<Long> memberIds;

    // 멤버 추가/삭제
    private List<Long> memberIdsToAdd;
    private List<Long> memberIdsToRemove;
}
