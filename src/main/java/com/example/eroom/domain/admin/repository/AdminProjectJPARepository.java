package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminProjectJPARepository extends JpaRepository<Project, Long> {

}
