package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminTaskJPARepository extends JpaRepository<Task, Long> {
    List<Task> findByDeleteStatus(DeleteStatus deleteStatus);
}
