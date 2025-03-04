package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.TaskStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskUpdateRequestDTO {

    @NotBlank(message = "테스크 이름은 필수입니다.")
    @Size(max = 50, message = "테스크 이름은 최대 50자까지 가능합니다.")
    private String title;

    @FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
    private LocalDateTime startDate;

    @Future(message = "종료 날짜는 미래여야 합니다.")
    private LocalDateTime endDate;

    @NotNull(message = "테스크의 상태를 선택해 주세요.")
    private TaskStatus status;

    @NotNull(message = "담당자는 반드시 지정해야 합니다.")
    private Long assignedMemberId; // 담당자 (participants 중에서 선택 가능)
    private List<Long> participantIds; // 참여자 목록 -> 참여자는 일단 없애는 것으로 변경
}
