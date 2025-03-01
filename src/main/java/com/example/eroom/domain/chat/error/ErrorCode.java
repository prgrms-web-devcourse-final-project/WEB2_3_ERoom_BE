package com.example.eroom.domain.chat.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Project
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PROJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "프로젝트에 접근할 수 없습니다."),
    INVALID_PROJECT_CREATOR(HttpStatus.FORBIDDEN, "프로젝트 생성자만 삭제할 수 있습니다."),
    PROJECT_UPDATE_DENIED(HttpStatus.FORBIDDEN, "프로젝트 생성자만 수정할 수 있습니다."),
    PROJECT_MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "프로젝트에 다른 멤버가 없어야 삭제할 수 있습니다."),
    USER_NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN, "해당 프로젝트의 멤버가 아닙니다."),
    PROJECT_CREATOR_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "프로젝트 생성자는 나갈 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // Chat
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    SENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "보낸 사람을 찾을 수 없습니다."),

    // Task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "업무가 존재하지 않습니다."),
    TASK_ACCESS_DENIED(HttpStatus.FORBIDDEN, "업무 수정 또는 삭제 권한이 없습니다."),
    TASK_ASSIGNEE_NOT_FOUND(HttpStatus.NOT_FOUND, "담당자가 존재하지 않습니다."),
    TASK_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "참여자가 존재하지 않습니다."),
    TASK_ASSIGNEE_MUST_BE_PARTICIPANT(HttpStatus.BAD_REQUEST, "담당자는 참여자 목록에 있어야 합니다."),

    // Category & Tag
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    SUBCATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "서브 카테고리를 찾을 수 없습니다."),
    TAG_NOT_BELONG_TO_SUBCATEGORY(HttpStatus.BAD_REQUEST, "태그가 해당 서브 카테고리에 속하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
