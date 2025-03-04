package com.example.eroom.domain.chat.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDetailDTO {

    private Long projectId;
    private String projectName;
    private String categoryName; // 카테고리 이름
    private List<SubCategoryDetail> subCategories; // 서브 카테고리 리스트
    // Task 목록
    private List<TaskDTO> tasks;
    // 참여 멤버 목록
    private List<MemberDTO> members;
}
