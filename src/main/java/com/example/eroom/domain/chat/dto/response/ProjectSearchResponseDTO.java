package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSearchResponseDTO {

    private Long id;
    private String name;
    private String creator;
    private ProjectStatus status;
    private DeleteStatus deleteStatus;
}
