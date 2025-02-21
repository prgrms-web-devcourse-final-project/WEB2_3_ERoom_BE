package com.example.eroom.domain.admin.dto.request;

import com.example.eroom.domain.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateTaskDTO {
    private String taskName;
    private TaskStatus taskStatus;
}
