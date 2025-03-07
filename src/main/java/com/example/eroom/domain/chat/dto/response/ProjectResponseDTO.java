package com.example.eroom.domain.chat.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String creatorUsername;
    private LocalDateTime createdAt;
}