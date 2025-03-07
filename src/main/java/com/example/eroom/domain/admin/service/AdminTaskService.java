package com.example.eroom.domain.admin.service;

import com.example.eroom.domain.admin.dto.request.AdminUpdateTaskDTO;
import com.example.eroom.domain.admin.dto.response.AdminTaskDTO;
import com.example.eroom.domain.admin.repository.AdminTaskJPARepository;
import com.example.eroom.domain.chat.repository.TaskRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Task;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminTaskService {
    private final AdminTaskJPARepository adminTaskJPARepository;
    private final TaskRepository taskRepository;

    public AdminTaskService(AdminTaskJPARepository adminTaskJPARepository, TaskRepository taskRepository) { this.adminTaskJPARepository = adminTaskJPARepository;
        this.taskRepository = taskRepository;
    }

    // [ 전체 활성화 업무 목록 ]
    public List<AdminTaskDTO> getActiveTasks() {
        return adminTaskJPARepository.findByDeleteStatus(DeleteStatus.ACTIVE)
                .stream()
                .map(AdminTaskDTO::new)
                .collect(Collectors.toList());
    }

    // [ 전체 비활성화 업무 목록 ]
    public List<AdminTaskDTO> getInActiveTasks() {
        return adminTaskJPARepository.findByDeleteStatus(DeleteStatus.DELETED)
                .stream()
                .map(AdminTaskDTO::new)
                .collect(Collectors.toList());
    }

    // [ 특정 업무 수정 ]
    @Transactional
    public AdminTaskDTO updateTask(Long taskId, AdminUpdateTaskDTO updatedTaskDTO) {
        // 1. 업무 존재 여부 확인
        Task existingTask = adminTaskJPARepository.findById(taskId)
                .orElseThrow( () -> new EntityNotFoundException( "해당 ID의 업무가 없습니다.: " + taskId));

        // 2. 값 업데이트 : 업무명, 진행 상태
        // 필드 값이 Null이 아닐 때만 업데이트 실행
        Task updatedTask = existingTask.toBuilder()
                .title(updatedTaskDTO.getTaskName() != null ?
                        updatedTaskDTO.getTaskName() : existingTask.getTitle())
                .status(updatedTaskDTO.getTaskStatus() != null ?
                        updatedTaskDTO.getTaskStatus() : existingTask.getStatus())
                .build();

        // 3. 변경된 데이터 저장
        Task savedTask = adminTaskJPARepository.save(updatedTask);

        return new AdminTaskDTO(savedTask);
    }

    // [ 특정 업무 수정 ]
    @Transactional
    public AdminTaskDTO activateTask(Long taskId) {
        // 1. 업무 존재 여부 확인
        Task task = adminTaskJPARepository.findById(taskId)
                .orElseThrow( () -> new EntityNotFoundException("해당 ID의 업무가 없습니다.: " + taskId));

        // 2. 비활성 -> 활성 전환
        task.activateTask();

        taskRepository.save(task);
        return new AdminTaskDTO(task);
    }

    // [ 특정 업무 삭제 ]
    public void deleteTask(Long taskId) {
        // 1. 업무 존재 여부 확인
        Task existingTask = adminTaskJPARepository.findById(taskId)
                .orElseThrow( () -> new EntityNotFoundException( "해당 ID의 업무가 없습니다.: " + taskId));

        adminTaskJPARepository.delete(existingTask);
    }



}
