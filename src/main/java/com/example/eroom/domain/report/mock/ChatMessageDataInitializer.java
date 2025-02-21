package com.example.eroom.domain.report.mock;

import com.example.eroom.domain.chat.repository.ChatRoomRepository;
import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.chat.repository.ProjectRepository;
import com.example.eroom.domain.entity.*;
import com.example.eroom.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatMessageDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void run(String... args) throws Exception {

        // 채팅 맴버 생성+저장
        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setEmail("qwerty1@naver.com");
        member1.setPassword("1234");

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setEmail("qwerty2@naver.com");
        member2.setPassword("1234");

        Member member3 = new Member();
        member3.setUsername("member3");
        member3.setEmail("qwerty3@naver.com");
        member3.setPassword("1234");

        Member member4 = new Member();
        member4.setUsername("member4");
        member4.setEmail("qwerty4@naver.com");
        member4.setPassword("1234");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // 백엔드 프로젝트
        Project project1 = new Project();
        project1.setStatus(ProjectStatus.BEFORE_START);
        project1.setCreator(member1);

        // 프론트엔드 프로젝트
        Project project2 = new Project();
        project2.setStatus(ProjectStatus.BEFORE_START);
        project2.setCreator(member1);

        // 프로젝트 저장
        projectRepository.save(project1);
        projectRepository.save(project2);

        // 백엔드 채팅방
        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setProject(project1);

        // 프론트엔드 채팅방
        ChatRoom chatRoom2 = new ChatRoom();
        chatRoom2.setProject(project2);

        // 채팅방 저장
        chatRoomRepository.save(chatRoom1);
        chatRoomRepository.save(chatRoom2);

        // 백엔트 채팅
        // 채팅 생성
        ChatMessage chatMessage1 = new ChatMessage();
        chatMessage1.setMessage("ㅎㅇ");
        chatMessage1.setChatRoom(chatRoom1);
        chatMessage1.setSender(member1);
        chatMessage1.setSentAt(LocalDateTime.now());

        // 프론트엔트 채팅
        // 채팅 생성
        ChatMessage chatMessage100 = new ChatMessage();
        chatMessage100.setMessage("오오");
        chatMessage100.setChatRoom(chatRoom2);
        chatMessage100.setSender(member1);
        chatMessage100.setSentAt(LocalDateTime.now());

        chatMessageRepository.save(chatMessage1);
        chatMessageRepository.save(chatMessage100);
    }
}