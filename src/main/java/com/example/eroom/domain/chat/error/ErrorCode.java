package com.example.eroom.domain.chat.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트가 존재하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PROJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "프로젝트에 접근할 수 없습니다."),
    INVALID_PROJECT_CREATOR(HttpStatus.FORBIDDEN, "프로젝트 생성자만 삭제할 수 있습니다."),
    PROJECT_MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "프로젝트에 다른 멤버가 없어야 삭제할 수 있습니다."),
    USER_NOT_PROJECT_MEMBER(HttpStatus.FORBIDDEN, "해당 프로젝트의 멤버가 아닙니다."),
    PROJECT_CREATOR_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "프로젝트 생성자는 나갈 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
