package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.repository.AdminProjectJPARepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Project;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminProjectService {
    private final AdminProjectJPARepository adminProjectJPARepository;
    private final ProjectRepository projectRepository;

    public AdminProjectService(AdminProjectJPARepository adminProjectJPARepository, ProjectRepository projectRepository) {
        this.adminProjectJPARepository = adminProjectJPARepository;
        this.projectRepository = projectRepository;
    }
    // [ 전체 활성화 프로젝트 목록 ]
    public List<AdminProjectDTO> getActiveProjects() {
        return adminProjectJPARepository.findByDeleteStatus(DeleteStatus.ACTIVE)
                .stream()
                .map(AdminProjectDTO::new)
                .collect(Collectors.toList());
    }
    // [ 전체 비활성화 프로젝트 목록 ]
    public List<AdminProjectDTO> getInActiveProjects() {
        return adminProjectJPARepository.findByDeleteStatus(DeleteStatus.DELETED)
                .stream()
                .map(AdminProjectDTO::new)
                .collect(Collectors.toList());
    }

    // [ 특정 프로젝트 수정 ]
    @Transactional
    public AdminProjectDTO updateProject(Long projectId, AdminUpdateProjectDTO updatedProjectDTO) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 빌더 패턴을 활용한 값 업데이트
        existingProject = existingProject.toBuilder()
                .name(updatedProjectDTO.getProjectName() != null ? updatedProjectDTO.getProjectName() : existingProject.getName())
                .status(updatedProjectDTO.getProjectStatus() != null ? updatedProjectDTO.getProjectStatus() : existingProject.getStatus())
                .build();

        // 3. 변경된 데이터 저장
        Project savedProject = adminProjectJPARepository.save(existingProject);

        return new AdminProjectDTO(savedProject);
    }

    // [ 특정 프로젝트 활성화 ]
    @Transactional
    public AdminProjectDTO activateProject(Long projectId) {
        // 1. 프로젝트 존재 여부 확인
        Project project = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 비활성 -> 활성 전환
        project.activateProject();

        projectRepository.save(project);
        return new AdminProjectDTO(project);
    }

    // [ 특정 프로젝트 삭제 ]
    public void deleteProject(Long projectId) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 프로젝트 삭제
        adminProjectJPARepository.delete(existingProject);
    }
}
