package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.ColorInfo;
import com.example.eroom.domain.entity.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskCreateRequestDTO {

    private Long projectId;

    @NotBlank(message = "테스크 이름은 필수입니다.")
    @Size(max = 50, message = "테스크 이름은 최대 50자까지 가능합니다.")
    private String title;

    @FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
    private LocalDateTime startDate;

    @Future(message = "종료 날짜는 미래여야 합니다.")
    private LocalDateTime endDate;

    private TaskStatus status;

    @NotNull(message = "담당자는 반드시 지정해야 합니다.")
    private Long assignedMemberId;

    private List<Long> participantIds;
    private ColorInfo colors;
}
