package com.example.eroom.domain.elasticsearch.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "project_index")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDocument {
    @Id
    private String id;

    private String name;
    private String description;
    private String status;
    private String category;
    private List<String> tags;
}
