package com.example.eroom.domain.elasticsearch;

import com.example.eroom.domain.entity.Project;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElasticsearchService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String ELASTICSEARCH_URL = "http://localhost:9200/projects/_doc/";

    public void savedProject(Project project) {
        String url = ELASTICSEARCH_URL + project.getId();
        Map<String, Object> document = new HashMap<>();
        document.put("id", project.getId());
        document.put("name", project.getName());
        document.put("description", project.getDescription());
        document.put("startDate", project.getStartDate());
        document.put("endDate", project.getEndDate());
        document.put("status", project.getStatus());
        document.put("creator", project.getCreator());

        // 프로젝트 태그 추가
        List<String> tags = project.getTags().stream()
                        .map(projectTag -> projectTag.getTag().getName())
                        .collect(Collectors.toList());

        document.put("tags", tags);

        restTemplate.postForEntity(url, document, String.class);
    }
}