package com.example.eroom.domain.elasticsearch.repository;

import com.example.eroom.domain.elasticsearch.Entity.ProjectES;
import com.example.eroom.domain.entity.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProjectEsRepository extends ElasticsearchRepository<ProjectES, Long> {

}
