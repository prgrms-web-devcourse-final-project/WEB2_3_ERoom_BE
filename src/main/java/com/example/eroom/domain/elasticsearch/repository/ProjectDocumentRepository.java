package com.example.eroom.domain.elasticsearch.repository;

import com.example.eroom.domain.elasticsearch.entity.ProjectDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDocumentRepository extends ElasticsearchRepository<ProjectDocument, String> {
    List<ProjectDocument> findAllBy();

}

