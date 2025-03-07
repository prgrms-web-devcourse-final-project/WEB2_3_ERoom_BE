package com.example.eroom.domain.elasticsearch.mapper;

import com.example.eroom.domain.elasticsearch.entity.ProjectDocument;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.ProjectSubCategory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProjectMapper {
    public static ProjectDocument toDocument(Project project) {
        return ProjectDocument.builder()
                .id(project.getId().toString())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus().toString())
                .category(project.getCategory().getName())
                .tags(project.getTags().stream()
                        .map(pt -> pt.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }
}
