package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.service.AdminProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manage/project")
public class AdminProjectController {
    private final AdminProjectService adminProjectService;
    public AdminProjectController(AdminProjectService adminProjectService) { this.adminProjectService = adminProjectService; }

    @GetMapping("/list")
    public ResponseEntity<List<AdminProjectDTO>> projectList() {
        // 전체 프로젝트 목록
        List<AdminProjectDTO> totalProjects = adminProjectService.getTotalProjects();
        return ResponseEntity.ok(totalProjects);
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
        adminProjectService.deleteProject(projectId);

        return ResponseEntity.noContent().build();
    }
}
