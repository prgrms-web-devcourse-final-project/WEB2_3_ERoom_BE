package com.example.eroom.domain.elasticsearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.eroom.domain.elasticsearch.entity.ProjectDocument;
import com.example.eroom.domain.elasticsearch.repository.ProjectDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectElasticService {
    private final ProjectDocumentRepository projectDocumentRepository;
    private final ElasticsearchClient client;

    public Map<String, Long> getTagCounts() throws IOException {
        // 1. Elasticsearch 검색 요청
        SearchResponse<Void> response = client.search( s -> s
                .index("project_index")
                .size(0)
                .aggregations("tag_counts", agg -> agg
                        .terms( t -> t
                                .field("tags.keyword")
                                .size(10)
                        )
                ),
                Void.class
        );

        // 2. 결과 파싱
        Map<String, Map<String, Long>> result = new HashMap<>();

        Map<String, Long> tagCountsMap = new HashMap<>();
        response.aggregations()
                .get("tag_counts")
                .sterms()
                .buckets()
                .array()
                .forEach(bucket -> tagCountsMap.put(bucket.key().stringValue(), bucket.docCount()));
        return tagCountsMap;
    }

    public List<ProjectDocument> getAllProjects(){
        return projectDocumentRepository.findAllBy();

    }
}
