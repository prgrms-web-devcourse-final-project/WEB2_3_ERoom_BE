package com.example.eroom.domain.admin.dto.request;

import com.example.eroom.domain.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateProjectDTO {
    private String projectName;
    private ProjectStatus projectStatus;

}
