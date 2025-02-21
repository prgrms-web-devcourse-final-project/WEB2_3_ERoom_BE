package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminProjectJPARepository extends JpaRepository<Project, Long> {
    List<Project> findByDeleteStatus(DeleteStatus deleteStatus);
}
