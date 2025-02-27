package com.example.eroom.domain.chat.repository;

import com.example.eroom.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
