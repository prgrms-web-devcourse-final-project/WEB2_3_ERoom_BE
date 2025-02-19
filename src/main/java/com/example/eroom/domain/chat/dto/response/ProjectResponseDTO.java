package com.example.eroom.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private LocalDateTime createdAt;
}