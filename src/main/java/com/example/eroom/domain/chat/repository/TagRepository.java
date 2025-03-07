package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAll();
}
