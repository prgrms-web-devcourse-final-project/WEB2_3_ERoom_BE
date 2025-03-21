package com.example.eroom.domain.chat.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
}
