package com.example.eroom.domain.chat.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDetailDTO {

    private Long projectId;
    private String projectName;

    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;

    // Task 목록
    private List<TaskDTO> tasks;

    // 참여 멤버 목록
    private List<MemberDTO> members;
}
