package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProject(Project project);
}

