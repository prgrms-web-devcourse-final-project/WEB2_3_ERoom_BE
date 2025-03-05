package com.example.eroom.domain.elasticsearch.controller;

import com.example.eroom.domain.elasticsearch.entity.ProjectDocument;
import com.example.eroom.domain.elasticsearch.service.ProjectElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elasticsearch")
@RequiredArgsConstructor
public class ProjectEsController {
    private final ProjectElasticService projectElasticService;

    @GetMapping("/tagcount")
    public ResponseEntity<Map<String, Long>> getTagCount() {
        try {
            return ResponseEntity.ok(projectElasticService.getTagCounts());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/taglist")
    public List<ProjectDocument> getProjects() {
        return projectElasticService.getAllProjects();
    }

}
