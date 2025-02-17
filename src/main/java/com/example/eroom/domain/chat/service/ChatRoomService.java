package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.ChatRoomMemberRepository;
import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public void createGroupChatRoom(Long projectId, ChatRoom chatRoom) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));
        chatRoom.setProject(project);
        chatRoom.setType(ChatRoomType.GROUP);
        chatRoomRepository.save(chatRoom);

        // 프로젝트 참여자 전원을 채팅방에 참여시킴
        List<ProjectMember> members = project.getMembers();
        for (ProjectMember member : members) {
            ChatRoomMember chatRoomMember = new ChatRoomMember();
            chatRoomMember.setChatRoom(chatRoom);
            chatRoomMember.setMember(member.getMember());
            chatRoomMemberRepository.save(chatRoomMember);
        }
    }

    public void createPrivateChatRoom(Long projectId, Member currentMember, Long targetUserId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + projectId));

        Member targetMember = memberRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + targetUserId));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setProject(project);
        chatRoom.setType(ChatRoomType.PRIVATE);
        chatRoom.setName(currentMember.getUsername() + " & " + targetMember.getUsername());
        chatRoomRepository.save(chatRoom);

        // 현재 사용자 추가
        ChatRoomMember currentUserMember = new ChatRoomMember();
        currentUserMember.setChatRoom(chatRoom);
        currentUserMember.setMember(currentMember);
        chatRoomMemberRepository.save(currentUserMember);

        // 상대방 추가
        ChatRoomMember targetUserMember = new ChatRoomMember();
        targetUserMember.setChatRoom(chatRoom);
        targetUserMember.setMember(targetMember);
        chatRoomMemberRepository.save(targetUserMember);
    }

    public ChatRoom getChatRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId).get();
    }
}