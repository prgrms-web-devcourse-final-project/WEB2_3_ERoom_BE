package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.error.CustomException;
import com.example.eroom.domain.chat.error.ErrorCode;
import com.example.eroom.domain.chat.repository.ChatRoomMemberRepository;
import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    // 단체 채팅방 생성 (프로젝트 생성 시 호출)
    public ChatRoom createGroupChatRoomForProject(Project project) {

        ChatRoom chatRoom = ChatRoom.builder()
                .project(project)
                .type(ChatRoomType.GROUP)
                .name(project.getName() + " [GROUP]")
                .createdAt(LocalDateTime.now())
                .build();

        // 채팅방 저장
        chatRoomRepository.save(chatRoom);

        // 프로젝트 참여자 전원을 채팅방에 참여시킴
        List<ProjectMember> projectMembers = project.getMembers();
        for (ProjectMember projectMember : projectMembers) {

            ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                    .chatRoom(chatRoom)
                    .member(projectMember.getMember())
                    .build();

            chatRoomMemberRepository.save(chatRoomMember);
        }

        return chatRoom;
    }

    // 1:1 채팅방 생성 -> 추후 추가 기능
    public ChatRoom createPrivateChatRoom(Long projectId, Member currentMember, Long targetUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));

        Member targetMember = memberRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + targetUserId));

        // ChatRoom 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .project(project)
                .type(ChatRoomType.PRIVATE)
                .name(currentMember.getUsername() + " & " + targetMember.getUsername())
                .createdAt(LocalDateTime.now())
                .build();

        chatRoomRepository.save(chatRoom);

        // 현재 사용자 추가
        ChatRoomMember currentUserMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(currentMember)
                .build();

        chatRoomMemberRepository.save(currentUserMember);

        // 상대방 추가
        ChatRoomMember targetUserMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(targetMember)
                .build();

        chatRoomMemberRepository.save(targetUserMember);

        return chatRoom;
    }

    // 채팅방 조회
    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
    }
}
