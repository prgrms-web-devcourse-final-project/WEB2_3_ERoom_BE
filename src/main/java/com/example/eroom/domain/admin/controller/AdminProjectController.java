package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.service.AdminProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/manage/project")
public class AdminProjectController {
    private final AdminProjectService adminProjectService;
    public AdminProjectController(AdminProjectService adminProjectService) { this.adminProjectService = adminProjectService; }

    @GetMapping("/list")
    public ResponseEntity<List<AdminProjectDTO>> projectList(@RequestParam Optional<String> deleteStatus) {
        List<AdminProjectDTO> tasks = deleteStatus
                .filter(s -> "deleted".equalsIgnoreCase(s))
                .map(s -> adminProjectService.getInActiveProjects())
                .orElse(adminProjectService.getActiveProjects());

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{projectId}/modify")
    public ResponseEntity<AdminProjectDTO> projectModify(
            @PathVariable Long projectId,
            @RequestBody AdminUpdateProjectDTO updateDTO) {

        AdminProjectDTO updatedProject = adminProjectService.updateProject(projectId, updateDTO);

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{projectId}/delete")
    public ResponseEntity<Void> projectDelete(@PathVariable Long projectId) {
        try {
            adminProjectService.deleteProject(projectId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
