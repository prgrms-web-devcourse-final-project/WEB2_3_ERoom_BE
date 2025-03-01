package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.dto.request.TaskCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.TaskUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.response.TaskUpdateResponseDTO;
import com.example.eroom.domain.chat.service.TaskService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Task 생성
    @PostMapping("/create")
    public ResponseEntity<Void> createTask(@Valid @RequestBody TaskCreateRequestDTO requestDTO,
                                           HttpSession session) {

        Member creator = (Member) session.getAttribute("member");
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Task 상세 조회 (수정 화면에 사용)
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskUpdateResponseDTO> getTask(@PathVariable Long taskId, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TaskUpdateResponseDTO taskResponse = taskService.getTask(taskId);
        return ResponseEntity.ok(taskResponse);
    }

    // Task 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId,
                                           @RequestBody TaskUpdateRequestDTO requestDTO,
                                           HttpSession session) {
        Member editor = (Member) session.getAttribute("member");
        if (editor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        taskService.updateTask(taskId, requestDTO, editor);
        return ResponseEntity.ok().build();
    }

    // Task 삭제 (soft delete)
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, HttpSession session) {
        Member editor = (Member) session.getAttribute("member");
        if (editor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        taskService.deleteTask(taskId, editor);
        return ResponseEntity.ok().build();
    }
}
