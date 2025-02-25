package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectUpdateResponseDTO {

    private Long id;
    private String name;
    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private List<MemberDTO> members;
}
