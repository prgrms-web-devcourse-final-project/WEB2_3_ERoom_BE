package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.repository.AdminProjectJPARepository;
import com.example.eroom.domain.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminProjectService {
    private final AdminProjectJPARepository adminProjectJPARepository;

    public AdminProjectService(AdminProjectJPARepository adminProjectJPARepository) {
        this.adminProjectJPARepository = adminProjectJPARepository;
    }

    // [ 전체 관리 프로젝트 목록 ]
    public List<AdminProjectDTO> getTotalProjects() {
        // 1. Project 엔티티 리스트 가져오기
        List<Project> projects = adminProjectJPARepository.findAll();

        // 2. Project 엔티티 -> AdminProjectDTO로 변환
        return projects.stream()
                .map(AdminProjectDTO::new)
                .collect(Collectors.toList());
    }

    // [ 특정 관리 프로젝트 수정 ]
    public AdminProjectDTO updateProject(Long projectId, AdminUpdateProjectDTO updatedProjectDTO) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new IllegalArgumentException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 값 업데이트 : 프로젝트 명, 진행 상태, 생성일, 기간
        existingProject.setName(updatedProjectDTO.getProjectName());
        existingProject.setStatus(updatedProjectDTO.getProjectStatus());
        existingProject.setStartDate(updatedProjectDTO.getStartDate());
        existingProject.setStartDate(updatedProjectDTO.getStartDate());
        existingProject.setEndDate(updatedProjectDTO.getEndDate());

        // 3. 변경된 데이터 저장
        Project savedProject = adminProjectJPARepository.save(existingProject);

        // 4. DTO로 변환하여 저장
        return new AdminProjectDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getStatus(),
                savedProject.getCreatedAt(),
                savedProject.getStartDate(),
                savedProject.getEndDate(),
                savedProject.getTag1(),
                savedProject.getTag2(),
                savedProject.getTag3()
        );
    }

    // [ 특정 관리 프로젝트 삭제 ]
    public void deleteProject(Long projectId) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new IllegalArgumentException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 프로젝트 삭제
        adminProjectJPARepository.delete(existingProject);
    }
}
