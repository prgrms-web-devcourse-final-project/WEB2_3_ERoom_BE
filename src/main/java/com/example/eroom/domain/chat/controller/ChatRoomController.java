package com.example.eroom.domain.chat.controller;

import com.example.eroom.domain.chat.service.ChatMessageService;
import com.example.eroom.domain.chat.service.ChatRoomService;
import com.example.eroom.domain.chat.service.MemberService;
import com.example.eroom.domain.chat.service.ProjectService;
import com.example.eroom.domain.entity.ChatMessage;
import com.example.eroom.domain.entity.ChatRoom;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.Project;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final ChatMessageService chatMessageService;

    // 채팅방 목록 보기 (프로젝트 상세 화면에서 사용)
    @GetMapping("/list/{projectId}")
    public String listChatRooms(@PathVariable Long projectId, Model model) {

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        model.addAttribute("chatRooms", project.getChatRooms());
        return "chatroom/list";
    }

    // 채팅방 생성 폼 (단체 채팅방)
    @GetMapping("/create/{projectId}")
    public String showCreateForm(@PathVariable Long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        model.addAttribute("chatRoom", new ChatRoom());
        return "chatroom/create";
    }

    // 단체 채팅방 생성 처리
    @PostMapping("/create/{projectId}")
    public String createGroupChatRoom(@PathVariable Long projectId,
                                      @ModelAttribute ChatRoom chatRoom) {
        chatRoomService.createGroupChatRoom(projectId, chatRoom);
        return "redirect:/project/" + projectId;
    }

    // 1:1 채팅방 생성 (프로젝트 참여자 목록에서 선택)
    @GetMapping("/create/private/{projectId}")
    public String showPrivateChatForm(@PathVariable Long projectId, Model model, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        Project project = projectService.getProjectById(projectId);
        List<Member> participants = projectService.getProjectParticipantsExceptCurrentMember(project, currentMember);

        model.addAttribute("project", project);
        model.addAttribute("participants", participants);
        return "chatroom/create-private";
    }

    @PostMapping("/create/private/{projectId}")
    public String createPrivateChatRoom(@PathVariable Long projectId,
                                        @RequestParam Long targetUserId,
                                        HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        chatRoomService.createPrivateChatRoom(projectId, currentMember, targetUserId);
        return "redirect:/project/" + projectId;
    }

    // 채팅방 입장
    @GetMapping("/chat/{roomId}")
    public String enterChatRoom(@PathVariable Long roomId, Model model, HttpSession session) {

        System.out.println("roomId: " + roomId);

        ChatRoom chatRoom = chatRoomService.getChatRoomById(roomId);
        Member currentMember = (Member) session.getAttribute("member");

        List<ChatMessage> messages = chatMessageService.getMessagesByRoomId(roomId);

        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("currentMember", currentMember);
        model.addAttribute("messages", messages);

        return "chatroom/room";
    }

}
