package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.dto.request.ProjectCreateRequestDTO;
import com.example.eroom.domain.chat.dto.request.ProjectUpdateRequestDTO;
import com.example.eroom.domain.chat.dto.request.SubCategoryRequest;
import com.example.eroom.domain.chat.dto.response.*;
import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import com.example.eroom.domain.chat.repository.*;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;
    private final ProjectSubCategoryRepository projectSubCategoryRepository;
    private final ProjectTagRepository projectTagRepository;

    // 현재 사용자가 참여 중인 프로젝트 목록 가져오기
    public List<Project> getProjectsByUser(Member member) {
//        return projectRepository.findByMembers_Member(member);
        return projectRepository.findByMembers_MemberAndDeleteStatus(member, DeleteStatus.ACTIVE);
    }

    // 프로젝트 상세
    public ProjectDetailChatDTO getProjectDetail(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectDetailChatDTO dto = new ProjectDetailChatDTO();
        dto.setProjectId(project.getId());
        dto.setProjectName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());

        // 단체 채팅방 가져오기
        ChatRoom groupChatRoom = project.getChatRooms().stream()
                .filter(chatRoom -> chatRoom.getType() == ChatRoomType.GROUP)
                .findFirst()
                .orElse(null);

        if (groupChatRoom != null) {
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            chatRoomDTO.setChatRoomId(groupChatRoom.getId());
            chatRoomDTO.setName(groupChatRoom.getName());
            chatRoomDTO.setType(groupChatRoom.getType());

            // 메시지 추가
            List<ChatMessageDTO> messages = groupChatRoom.getMessages().stream()
                    .map(message -> {
                        ChatMessageDTO messageDTO = new ChatMessageDTO();
                        messageDTO.setMessageId(message.getId());
                        messageDTO.setChatRoomId(groupChatRoom.getId());
                        messageDTO.setSenderId(message.getSender().getId());
                        messageDTO.setSenderName(message.getSender().getUsername());
                        messageDTO.setSenderProfile(message.getSender().getProfile());
                        messageDTO.setMessage(message.getMessage());
                        messageDTO.setSentAt(message.getSentAt());
                        return messageDTO;
                    })
                    .collect(Collectors.toList());

            chatRoomDTO.setMessages(messages);
            dto.setGroupChatRoom(chatRoomDTO);
        }

        return dto;
    }

    public Project createProject(ProjectCreateRequestDTO dto, Member creator) {

        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setStatus(ProjectStatus.BEFORE_START); // 프로젝트 상태(기본값 : 시작 전)
        project.setCreator(creator); // 프로젝트 생성자

        // 카테고리 설정
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        project.setCategory(category);

        // 서브 카테고리와 태그 설정
        if (dto.getSubCategories() != null && !dto.getSubCategories().isEmpty()) {
            for (SubCategoryRequest subCategoryRequest : dto.getSubCategories()) {
                // 서브 카테고리 조회
                SubCategory subCategory = subCategoryRepository.findById(subCategoryRequest.getSubCategoryId())
                        .orElseThrow(() -> new CustomException(ErrorCode.SUBCATEGORY_NOT_FOUND));


                // 프로젝트, 서브카테고리 연결 엔티티 생성
                ProjectSubCategory projectSubCategory = new ProjectSubCategory();
                projectSubCategory.setProject(project);
                projectSubCategory.setSubCategory(subCategory);

                // 프로젝트에 ProjectSubCategory 추가
                project.getProjectSubCategories().add(projectSubCategory);

                // 태그 처리
                if (subCategoryRequest.getTagIds() != null && !subCategoryRequest.getTagIds().isEmpty()) {
                    List<Tag> tags = tagRepository.findAllById(subCategoryRequest.getTagIds());

                    List<Tag> updatedTags = new ArrayList<>();

                    for (Tag tag : tags) {
                        // 태그가 해당 서브카테고리에 속하는지 확인
                        if (tag.getSubCategory().getId().equals(subCategory.getId())) {

                            // 태그 사용 횟수 증가
                            tag.setCount(tag.getCount() + 1);
                            updatedTags.add(tag);

                            // 프로젝트, 태그 연결 엔티티 생성
                            ProjectTag projectTag = new ProjectTag();
                            projectTag.setProject(project);
                            projectTag.setTag(tag);

                            // 프로젝트에 ProjectTag 추가
                            project.getTags().add(projectTag);
                        } else {
                            throw new CustomException(ErrorCode.TAG_NOT_BELONG_TO_SUBCATEGORY);
                        }
                    }
                    // 태그들 한번에 저장
                    tagRepository.saveAll(updatedTags);
                }
            }
        }

        // 프로젝트 생성할 때 랜덤한 color
        project.setColors(dto.getColors() != null ? dto.getColors() : new ColorInfo("#FFFFFF", "#000000"));

        // 프로젝트 생성자를 프로젝트 멤버로 추가
        ProjectMember creatorMember = new ProjectMember();
        creatorMember.setProject(project);
        creatorMember.setMember(creator);
        creatorMember.setJoinedAt(LocalDateTime.now());
        project.getMembers().add(creatorMember);

        List<Member> invitedMembers = memberRepository.findAllById(dto.getInvitedMemberIds());

        for (Member member : invitedMembers) {

            ProjectMember projectMember = new ProjectMember();
            projectMember.setProject(project);
            projectMember.setMember(member);
            projectMember.setJoinedAt(LocalDateTime.now());
            project.getMembers().add(projectMember);
        }

        // 프로젝트 저장
        Project savedProject = projectRepository.save(project);

        // 프로젝트 초대 알림 보내기
        for (Member member : invitedMembers) {
            if(!project.getCreator().getId().equals(member.getId())) {
                String message = "새로운 프로젝트에 초대되었습니다: " + savedProject.getName();
                notificationService.createNotification(member, message, NotificationType.PROJECT_INVITE, savedProject.getId());// 알림생성, 저장, 알림 전송
            }
        }

        return savedProject;
    }

    public ProjectUpdateResponseDTO getProjectForEdit(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectUpdateResponseDTO dto = new ProjectUpdateResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setCategoryName(project.getCategory().getName());

        // 서브 카테고리와 태그 정보 변환
        List<SubCategoryDetail> subCategoryDetails = new ArrayList<>();
        for (ProjectSubCategory psc : project.getProjectSubCategories()) {
            SubCategory subCategory = psc.getSubCategory();
            SubCategoryDetail detail = new SubCategoryDetail();
            detail.setId(subCategory.getId());
            detail.setName(subCategory.getName());

            // 해당 서브 카테고리에 선택된 태그들 찾기
            List<TagDetail> tagDetails = project.getTags().stream()
                    .filter(pt -> pt.getTag().getSubCategory().getId().equals(subCategory.getId()))
                    .map(pt -> {
                        TagDetail td = new TagDetail();
                        td.setId(pt.getTag().getId());
                        td.setName(pt.getTag().getName());
                        return td;
                    })
                    .collect(Collectors.toList());

            detail.setTags(tagDetails);
            subCategoryDetails.add(detail);
        }

        dto.setSubCategories(subCategoryDetails);
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());

        List<MemberDTO> memberDTOs = project.getMembers().stream()
                .map(pm -> new MemberDTO(pm.getMember().getId(), pm.getMember().getUsername(), pm.getMember().getProfile()))
                .collect(Collectors.toList());

        dto.setMembers(memberDTOs);

        return dto;
    }

    public void updateProject(Long projectId, ProjectUpdateRequestDTO projectUpdateRequestDTO, Member editor) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        // 수정 권한 확인: 프로젝트 생성자만 가능
        if (!project.getCreator().getId().equals(editor.getId())) {
            throw new CustomException(ErrorCode.PROJECT_UPDATE_DENIED);
        }

        // 이름 수정
        if (projectUpdateRequestDTO.getName() != null) {
            project.setName(projectUpdateRequestDTO.getName());
        }

        // 카테고리 수정 (기존 데이터 제거 후 변경)
        if (projectUpdateRequestDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(projectUpdateRequestDTO.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

            // 기존의 서브 카테고리 및 태그 제거
            project.getProjectSubCategories().clear();
            project.getTags().clear();
            projectRepository.flush();

            project.setCategory(category);
        }

        // 서브 카테고리 및 태그 수정
        if (projectUpdateRequestDTO.getSubCategories() != null) {
            // 기존 서브 카테고리 연결 제거
            project.getProjectSubCategories().clear();

            // 기존 태그 연결 제거
            project.getTags().clear();

            // 기존 서브 카테고리 및 태그 삭제
            projectSubCategoryRepository.deleteAllByProject(project);
            project.getProjectSubCategories().clear();
            projectTagRepository.deleteAllByProject(project);
            project.getTags().clear();
            projectRepository.flush();

            // 새로운 서브 카테고리 및 태그 추가
            for (SubCategoryRequest subCategoryRequest : projectUpdateRequestDTO.getSubCategories()) {
                SubCategory subCategory = subCategoryRepository.findById(subCategoryRequest.getSubCategoryId())
                        .orElseThrow(() -> new CustomException(ErrorCode.SUBCATEGORY_NOT_FOUND));

                ProjectSubCategory projectSubCategory = new ProjectSubCategory();
                projectSubCategory.setProject(project);
                projectSubCategory.setSubCategory(subCategory);
                project.getProjectSubCategories().add(projectSubCategory);

                // 태그 처리
                if (subCategoryRequest.getTagIds() != null && !subCategoryRequest.getTagIds().isEmpty()) {
                    List<Tag> tags = tagRepository.findAllById(subCategoryRequest.getTagIds());

                    List<Tag> updatedTags = new ArrayList<>();

                    for (Tag tag : tags) {

                        // 태그 사용 횟수 증가
                        tag.setCount(tag.getCount() + 1);
                        updatedTags.add(tag);

                        if (tag.getSubCategory().getId().equals(subCategory.getId())) {
                            ProjectTag projectTag = new ProjectTag();
                            projectTag.setProject(project);
                            projectTag.setTag(tag);
                            project.getTags().add(projectTag);
                        } else {
                            throw new CustomException(ErrorCode.TAG_NOT_BELONG_TO_SUBCATEGORY);
                        }
                    }

                    // 태그들 한번에 저장
                    tagRepository.saveAll(updatedTags);
                }
            }
        }

        // 시작일, 종료일 수정
        project.setStartDate(projectUpdateRequestDTO.getStartDate());
        project.setEndDate(projectUpdateRequestDTO.getEndDate());

        // 상태 수정
        project.setStatus(projectUpdateRequestDTO.getStatus());

        // 멤버 교체
        if (projectUpdateRequestDTO.getMemberIds() != null) {
            // 기존 멤버 삭제 (remove 사용)
            project.getMembers().clear();
            projectRepository.flush();

            // 새로운 멤버 추가
            List<Member> newMembers = memberRepository.findAllById(projectUpdateRequestDTO.getMemberIds());
            List<ProjectMember> updatedProjectMembers = newMembers.stream().map(member -> {
                ProjectMember projectMember = new ProjectMember();
                projectMember.setProject(project);
                projectMember.setMember(member);
                projectMember.setJoinedAt(LocalDateTime.now());
                return projectMember;
            }).collect(Collectors.toList());

            project.getMembers().addAll(updatedProjectMembers);
        }

        // 멤버 추가
        if (projectUpdateRequestDTO.getMemberIdsToAdd() != null && !projectUpdateRequestDTO.getMemberIdsToAdd().isEmpty()) {
            List<Member> membersToAdd = memberRepository.findAllById(projectUpdateRequestDTO.getMemberIdsToAdd());

            for (Member member : membersToAdd) {
                // 이미 멤버로 추가되어 있는지 확인
                boolean alreadyMember = project.getMembers().stream()
                        .anyMatch(pm -> pm.getMember().getId().equals(member.getId()));
                if (!alreadyMember) {
                    ProjectMember projectMember = new ProjectMember();
                    projectMember.setProject(project);
                    projectMember.setMember(member);
                    projectMember.setJoinedAt(LocalDateTime.now());
                    project.getMembers().add(projectMember);
                }
            }
        }

        // 멤버 삭제
        if (projectUpdateRequestDTO.getMemberIdsToRemove() != null && !projectUpdateRequestDTO.getMemberIdsToRemove().isEmpty()) {
            List<Long> idsToRemove = projectUpdateRequestDTO.getMemberIdsToRemove();

            project.getMembers().removeIf(pm -> idsToRemove.contains(pm.getMember().getId()));
        }

        projectRepository.save(project);
    }

    public void softDeleteProject(Long projectId, Member currentMember) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        // 프로젝트 생성자가 아닌 경우 예외 발생
        if (!project.getCreator().getId().equals(currentMember.getId())) {
            System.out.println("프로젝트 생성자만 삭제할 수 있습니다.");
            throw new CustomException(ErrorCode.INVALID_PROJECT_CREATOR);
        }

        // 프로젝트에 속한 멤버가 생성자 혼자만 있는 경우에만 삭제 가능
        if (project.getMembers().size() > 1) {
            System.out.println("프로젝트에 다른 멤버가 없어야 삭제할 수 있습니다.");
            throw new CustomException(ErrorCode.PROJECT_MEMBER_EXISTS);
        }

        project.setDeleteStatus(DeleteStatus.DELETED);
        projectRepository.save(project);
    }


    public Project getProjectById(Long projectId) {

        return projectRepository.findByIdWithMembers(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
    }

    public boolean isMemberOfProject(Member currentMember, Long projectId) {
        Project project = getProjectById(projectId);
        return project.getMembers().stream()
                .anyMatch(member -> member.getMember().getId().equals(currentMember.getId()));
    }

    public ProjectDetailDTO getProjectDetailForView(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        ProjectDetailDTO dto = new ProjectDetailDTO();
        dto.setProjectId(project.getId());
        dto.setProjectName(project.getName());

        // 카테고리 정보 설정
        dto.setCategoryName(project.getCategory().getName());

        // 서브 카테고리 설정
        List<SubCategoryDetail> subCategoryDetails = project.getProjectSubCategories().stream()
                .map(projectSubCategory -> {
                    SubCategoryDetail detail = new SubCategoryDetail();
                    detail.setId(projectSubCategory.getSubCategory().getId());
                    detail.setName(projectSubCategory.getSubCategory().getName());

                    // 해당 서브카테고리에 속한 태그들만 필터링
                    List<TagDetail> tagDetails = project.getTags().stream()
                            .filter(tag -> tag.getTag().getSubCategory().getId().equals(projectSubCategory.getSubCategory().getId()))
                            .map(projectTag -> {
                                TagDetail tagDetail = new TagDetail();
                                tagDetail.setId(projectTag.getTag().getId());
                                tagDetail.setName(projectTag.getTag().getName());
                                return tagDetail;
                            })
                            .collect(Collectors.toList());

                    detail.setTags(tagDetails);
                    return detail;
                })
                .collect(Collectors.toList());

        dto.setSubCategories(subCategoryDetails);

        // Task 정보 추가 (데이터베이스에서 필터링)
        List<Task> activeTasks = taskRepository.findByProjectIdAndDeleteStatus(projectId, DeleteStatus.ACTIVE);
        List<TaskDTO> taskDTOList = activeTasks.stream().map(task -> {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTaskId(task.getId());
            taskDTO.setTitle(task.getTitle());
            taskDTO.setStartDate(task.getStartDate());
            taskDTO.setEndDate(task.getEndDate());
            taskDTO.setStatus(task.getStatus());

            taskDTO.setAssignedMemberName(task.getAssignedMember() != null ? task.getAssignedMember().getUsername() : null);

            List<String> participantNames = task.getParticipants().stream()
                    .map(taskMember -> taskMember.getMember().getUsername())
                    .collect(Collectors.toList());
            taskDTO.setParticipants(participantNames);
            // task 생성할 때 랜덤한 color
            taskDTO.setColors(task.getColors() != null ? task.getColors() : new ColorInfo("#FFFFFF", "#000000"));
            //taskDTO.setColors(task.getColors()); // color 정보 추가

            return taskDTO;
        }).collect(Collectors.toList());

//        // Task 정보 추가 (필터로 하는 방식)
//        List<TaskDTO> taskDTOList = project.getTasks().stream()
//                .filter(task -> task.getDeleteStatus() == DeleteStatus.ACTIVE)
//                .map(task -> {
//                    TaskDTO taskDTO = new TaskDTO();
//                    taskDTO.setTaskId(task.getId());
//                    taskDTO.setTitle(task.getTitle());
//                    taskDTO.setStartDate(task.getStartDate());
//                    taskDTO.setEndDate(task.getEndDate());
//                    taskDTO.setStatus(task.getStatus());
//
//                    // 담당자 이름
//                    taskDTO.setAssignedMemberName(task.getAssignedMember() != null ? task.getAssignedMember().getUsername() : null);
//
//                    // 참여자 이름 목록
//                    List<String> participantNames = task.getParticipants().stream()
//                            .map(taskMember -> taskMember.getMember().getUsername())
//                            .collect(Collectors.toList());
//                    taskDTO.setParticipants(participantNames);
//
//                    return taskDTO;
//                }).collect(Collectors.toList());

        dto.setTasks(taskDTOList);

        // 참여 멤버 추가
        List<MemberDTO> memberDTOs = project.getMembers().stream()
                .map(pm -> new MemberDTO(pm.getMember().getId(), pm.getMember().getUsername(), pm.getMember().getProfile()))
                .collect(Collectors.toList());

        dto.setMembers(memberDTOs);

        return dto;
    }

    public void leaveProject(Long projectId, Member member) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

        // 현재 사용자가 이 프로젝트의 멤버인지 확인
        boolean isMember = project.getMembers().stream()
                .anyMatch(pm -> pm.getMember().getId().equals(member.getId()));
        if (!isMember) {
            throw new CustomException(ErrorCode.USER_NOT_PROJECT_MEMBER);
        }

        // 프로젝트 생성자는 나갈 수 없음 (프로젝트 소유권을 이전 하던가 해야함)
        if (project.getCreator().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.PROJECT_CREATOR_CANNOT_LEAVE);
        }

        // 해당 멤버가 담당자로 설정된 Task 찾기
        List<Task> assignedTasks = project.getTasks().stream()
                .filter(task -> task.getAssignedMember() != null && task.getAssignedMember().getId().equals(member.getId()))
                .collect(Collectors.toList());

        // 프로젝트 생성자
        Member projectCreator = project.getCreator();

        // 담당자 변경 (담당자를 프로젝트 생성자로 변경)
        for (Task task : assignedTasks) {
            task.setAssignedMember(projectCreator);
        }

        // 멤버 제거
        project.getMembers().removeIf(pm -> pm.getMember().getId().equals(member.getId()));
        projectRepository.save(project);
    }

    @Scheduled(cron = "0 */10 * * * ?") // 매 시간 정각(00:00, 01:00, 02:00...) 실행
    public void sendEndDateReminder() {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime startOfNextDay = now.plusHours(24).withNano(0); // 정확히 24시간 후
        LocalDateTime endOfNextDay = startOfNextDay.plusMinutes(10).withNano(0); // 1시간 범위

        List<Project> projects = projectRepository.findProjectsEndingIn24Hours(startOfNextDay, endOfNextDay);

        log.info("24시간 후 종료될 프로젝트 수: " + projects.size());

        for (Project project : projects) {
            log.info("알림 전송 대상 프로젝트: " + project.getName());
            for(ProjectMember projectMember : project.getMembers()){
                String message = "프로젝트가 마감 24시간 전입니다: " + project.getName();
                notificationService.createNotification(projectMember.getMember(), message, NotificationType.PROJECT_EXIT, project.getId());// 알림생성, 저장, 알림 전송
            }
        }
    }
}
