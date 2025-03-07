package com.example.eroom.domain.auth.mock;

import com.example.eroom.domain.auth.repository.AuthMemberRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthMemberDataInitializer implements CommandLineRunner {
    private final AuthMemberRepository authMemberRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Member> members = List.of(
                Member.builder()
                        .username("지현")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("qkrwlgus4001@gmail.com")
                        .organization("지현소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("수호")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("yousuho1004@gmail.com")
                        .organization("수호소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("영신")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("dudtls0522@gmail.com")
                        .organization("영신소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("휘연")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("joybullyk@gmail.com")
                        .organization("휘연소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("송원")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("swsung91@gmail.com")
                        .organization("송원소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("규혁")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("gyuheak@gmail.com")
                        .organization("규혁소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build(),
                Member.builder()
                        .username("선형")
                        .memberGrade(MemberGrade.ADMIN)
                        .email("shtest529@gmail.com")
                        .organization("선형소속")
                        .deleteStatus(DeleteStatus.ACTIVE)
                        .createdAt(LocalDate.now())
                        .build()
        );

        for (Member member : members) {
            if (authMemberRepository.findByEmail(member.getEmail()).isEmpty()) {
                authMemberRepository.save(member);
                System.out.println("Admin 계정 추가: " + member.getEmail());
            } else {
                System.out.println("Admin 계정 이미 존재: " + member.getEmail());
            }
        }
    }
}

