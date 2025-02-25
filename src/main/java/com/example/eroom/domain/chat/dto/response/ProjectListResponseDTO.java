package com.example.eroom.domain.chat.dto.response;

import com.example.eroom.domain.entity.ColorInfo;
import com.example.eroom.domain.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectListResponseDTO {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String category;
    private List<String> subCategories1;
    private List<String> subCategories2;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProjectStatus status;
    private List<MemberDTO> members;
//    private List<String> memberNames;
//    private List<String> memberProfiles; // 프로필 추가
    private Long chatRoomId;
    private double progressRate;
    private ColorInfo colors;
    private Long creatorId;
}