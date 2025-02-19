package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.service.TaskService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Task 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createTask(@RequestBody TaskCreateRequestDTO requestDTO,
                                           HttpSession session) {
        Member creator = (Member) session.getAttribute("member");
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
