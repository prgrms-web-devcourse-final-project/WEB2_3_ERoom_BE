package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.repository.AdminProjectJPARepository;
import com.example.eroom.domain.entity.Project;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    @Transactional
    public AdminProjectDTO updateProject(Long projectId, AdminUpdateProjectDTO updatedProjectDTO) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 값 업데이트 : 프로젝트 명, 진행 상태, 생성일, 기간
        // 필드 값이 Null이 아닐 때만 업데이트 실행
        if (updatedProjectDTO.getProjectName() != null) {
            existingProject.setName(updatedProjectDTO.getProjectName());
        }
        if (updatedProjectDTO.getProjectStatus() != null) {
            existingProject.setStatus(updatedProjectDTO.getProjectStatus());
        }
        if (updatedProjectDTO.getStartDate() != null) {
            existingProject.setStartDate(updatedProjectDTO.getStartDate());
        }
        if (updatedProjectDTO.getEndDate() != null) {
            existingProject.setEndDate(updatedProjectDTO.getEndDate());
        }

        // 3. 변경된 데이터 저장
        Project savedProject = adminProjectJPARepository.save(existingProject);

        return new AdminProjectDTO(savedProject);
    }

    // [ 특정 관리 프로젝트 삭제 ]
    public void deleteProject(Long projectId) {
        // 1. 프로젝트 존재 여부 확인
        Project existingProject = adminProjectJPARepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 프로젝트가 없습니다.: " + projectId));

        // 2. 프로젝트 삭제
        adminProjectJPARepository.delete(existingProject);
    }
}
