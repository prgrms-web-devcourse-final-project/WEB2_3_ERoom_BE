package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.request.AdminUpdateTaskDTO;
import com.example.eroom.domain.admin.dto.response.AdminTaskDTO;
import com.example.eroom.domain.admin.service.AdminTaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage/task")
public class AdminTaskController {
    private final AdminTaskService adminTaskService;
    public AdminTaskController(AdminTaskService adminTaskService) { this.adminTaskService = adminTaskService; }

    @GetMapping("/list")
    public ResponseEntity<List<AdminTaskDTO>> taskList(@RequestParam Optional<String> deleteStatus) {
        List<AdminTaskDTO> tasks = deleteStatus
                .filter(s -> "deleted".equalsIgnoreCase(s))
                .map(s -> adminTaskService.getInActiveTasks())
                .orElse(adminTaskService.getActiveTasks());

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/modify")
    public ResponseEntity<AdminTaskDTO> taskModify(
            @PathVariable Long taskId,
            @RequestBody AdminUpdateTaskDTO updateDTO) {

        AdminTaskDTO updatedTask = adminTaskService.updateTask(taskId, updateDTO);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<Void> taskDelete(@PathVariable Long taskId) {
        try {
            adminTaskService.deleteTask(taskId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
