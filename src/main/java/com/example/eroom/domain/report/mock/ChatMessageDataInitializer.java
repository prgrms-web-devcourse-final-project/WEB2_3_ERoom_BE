package com.example.eroom.domain.report.mock;

import com.example.eroom.domain.chat.repository.*;
import com.example.eroom.domain.entity.*;
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
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        // 채팅 맴버 생성+저장
        Member member1 = new Member();
        member1.setUsername("member5");
        member1.setEmail("qwerty5@gmail.com");
        member1.setPassword("1234");

        Member member2 = new Member();
        member2.setUsername("member6");
        member2.setEmail("qwerty6@gmail.com");
        member2.setPassword("1234");

        Member member3 = new Member();
        member3.setUsername("member7");
        member3.setEmail("qwerty7@gmail.com");
        member3.setPassword("1234");

        Member member4 = new Member();
        member4.setUsername("member8");
        member4.setEmail("qwerty8@gmail.com");
        member4.setPassword("1234");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        Category category1 = new Category();
        category1.setName("category1");

        categoryRepository.save(category1);

        // 백엔드 프로젝트
        Project project1 = new Project();
        project1.setStatus(ProjectStatus.BEFORE_START);
        project1.setCreator(member1);
        project1.setCategory(category1);

        // 프론트엔드 프로젝트
        Project project2 = new Project();
        project2.setStatus(ProjectStatus.BEFORE_START);
        project2.setCreator(member1);
        project2.setCategory(category1);

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
        chatMessage1.setMessage("이번 프로젝트에서 도입할 아키텍처에 대해 이야기해보죠. " +
                "Monolithic으로 갈지, MSA로 갈지 고민이네요.");
        chatMessage1.setChatRoom(chatRoom1);
        chatMessage1.setSender(member1);
        chatMessage1.setSentAt(LocalDateTime.now().plusMinutes(2));

        ChatMessage chatMessage2 = new ChatMessage();
        chatMessage2.setMessage("Spring Boot 3.x랑 MSA 조합이면 괜찮을 것 같은데요. " +
                "하지만 트래픽이 크지 않다면 Monolithic이 유지보수도 쉽고 좋을 수도 있어요.");
        chatMessage2.setChatRoom(chatRoom1);
        chatMessage2.setSender(member2);
        chatMessage2.setSentAt(LocalDateTime.now().plusMinutes(4));

        ChatMessage chatMessage3 = new ChatMessage();
        chatMessage3.setMessage("DB 설계 측면에서도 Monolithic이면 하나의 트랜잭션에서 관리하기 편하고, " +
                "MSA면 서비스 간 데이터 일관성 관리가 중요해지겠죠. " +
                "특히 Saga 패턴 같은 걸 고민해야 할 수도 있어요.");
        chatMessage3.setChatRoom(chatRoom1);
        chatMessage3.setSender(member3);
        chatMessage3.setSentAt(LocalDateTime.now().plusMinutes(6));

        ChatMessage chatMessage4 = new ChatMessage();
        chatMessage4.setMessage("인프라 관점에서 보면, MSA는 Kubernetes 같은 오케스트레이션 툴이 필요하고, " +
                "서비스 간 통신 비용이 발생할 수 있어요. " +
                "CI/CD 파이프라인도 좀 더 신경 써야겠고요.");
        chatMessage4.setChatRoom(chatRoom1);
        chatMessage4.setSender(member4);
        chatMessage4.setSentAt(LocalDateTime.now().plusMinutes(8));

        ChatMessage chatMessage5 = new ChatMessage();
        chatMessage5.setMessage("그렇죠. 비용과 유지보수를 고려하면 Monolithic이 나을 수도 있고, " +
                "장기적으로 확장성을 생각하면 MSA가 유리할 수도 있겠네요. " +
                "혹시 지금 프로젝트 트래픽 예측 자료 있나요?");
        chatMessage5.setChatRoom(chatRoom1);
        chatMessage5.setSender(member1);
        chatMessage5.setSentAt(LocalDateTime.now().plusMinutes(10));

        ChatMessage chatMessage6 = new ChatMessage();
        chatMessage6.setMessage("네, 예측 트래픽을 보니까 초반에는 1000 TPS 정도인데, " +
                "3개월 후에는 5000 TPS까지 증가할 가능성이 있다고 해요. " +
                "이 정도면 MSA를 고려해야 할 것 같은데요?");
        chatMessage6.setChatRoom(chatRoom1);
        chatMessage6.setSender(member2);
        chatMessage6.setSentAt(LocalDateTime.now().plusMinutes(12));

        ChatMessage chatMessage7 = new ChatMessage();
        chatMessage7.setMessage("그러면 DB도 분산 구성을 고려해야겠네요. " +
                "초기에는 PostgreSQL 단일 인스턴스로 가고, " +
                "추후 샤딩이나 CQRS를 도입하는 방식은 어떨까요?");
        chatMessage7.setChatRoom(chatRoom1);
        chatMessage7.setSender(member3);
        chatMessage7.setSentAt(LocalDateTime.now().plusMinutes(14));

        ChatMessage chatMessage8 = new ChatMessage();
        chatMessage8.setMessage("그럼 AWS RDS로 시작하고, " +
                "트래픽 증가하면 Aurora로 스케일링하는 방안도 가능하겠네요. " +
                "그리고 API Gateway랑 Service Mesh도 같이 검토해야 할 것 같아요.");
        chatMessage8.setChatRoom(chatRoom1);
        chatMessage8.setSender(member4);
        chatMessage8.setSentAt(LocalDateTime.now().plusMinutes(16));

        ChatMessage chatMessage9 = new ChatMessage();
        chatMessage9.setMessage("좋아요. " +
                "일단 Monolithic으로 시작하고, " +
                "트래픽 증가하면 점진적으로 MSA로 전환하는 방향으로 정리하죠. " +
                "오늘 논의한 내용은 제가 정리해서 공유하겠습니다!");
        chatMessage9.setChatRoom(chatRoom1);
        chatMessage9.setSender(member1);
        chatMessage9.setSentAt(LocalDateTime.now().plusMinutes(18));

        ChatMessage chatMessage10 = new ChatMessage();
        chatMessage10.setMessage("네, 정리되면 문서 공유 부탁드려요!");
        chatMessage10.setChatRoom(chatRoom1);
        chatMessage10.setSender(member2);
        chatMessage10.setSentAt(LocalDateTime.now().plusMinutes(20));

        ChatMessage chatMessage11 = new ChatMessage();
        chatMessage11.setMessage("좋습니다. DB 설계 부분도 더 고민해볼게요.");
        chatMessage11.setChatRoom(chatRoom1);
        chatMessage11.setSender(member3);
        chatMessage11.setSentAt(LocalDateTime.now().plusMinutes((22)));

        ChatMessage chatMessage12 = new ChatMessage();
        chatMessage12.setMessage("네, 인프라도 CI/CD 포함해서 검토하겠습니다.");
        chatMessage12.setChatRoom(chatRoom1);
        chatMessage12.setSender(member4);
        chatMessage12.setSentAt(LocalDateTime.now().plusMinutes(24));

        ChatMessage chatMessage13 = new ChatMessage();
        chatMessage13.setMessage("그럼 오늘 회의는 여기까지 하죠. 다들 수고하셨습니다!");
        chatMessage13.setChatRoom(chatRoom1);
        chatMessage13.setSender(member1);
        chatMessage13.setSentAt(LocalDateTime.now().plusMinutes(26));

        // 프론트엔트 채팅
        // 채팅 생성
        ChatMessage chatMessage100 = new ChatMessage();
        chatMessage100.setMessage("오오");
        chatMessage100.setChatRoom(chatRoom2);
        chatMessage100.setSender(member1);
        chatMessage100.setSentAt(LocalDateTime.now());

        chatMessageRepository.save(chatMessage1);
        chatMessageRepository.save(chatMessage2);
        chatMessageRepository.save(chatMessage3);
        chatMessageRepository.save(chatMessage4);
        chatMessageRepository.save(chatMessage5);
        chatMessageRepository.save(chatMessage6);
        chatMessageRepository.save(chatMessage7);
        chatMessageRepository.save(chatMessage8);
        chatMessageRepository.save(chatMessage9);
        chatMessageRepository.save(chatMessage10);
        chatMessageRepository.save(chatMessage11);
        chatMessageRepository.save(chatMessage12);
        chatMessageRepository.save(chatMessage13);

        chatMessageRepository.save(chatMessage100);
    }
}