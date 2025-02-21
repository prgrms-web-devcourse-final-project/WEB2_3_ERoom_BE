package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);

//    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.deleteStatus = 'ACTIVE'")
//    List<Task> findActiveTasksByProjectId(@Param("projectId") Long projectId);

    List<Task> findByProjectIdAndDeleteStatus(Long projectId, DeleteStatus deleteStatus);
}

