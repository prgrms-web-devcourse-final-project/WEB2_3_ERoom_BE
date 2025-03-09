package com.example.eroom.domain.chat.mock;

import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MemberDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {

        if (memberRepository.count() > 0) {
            System.out.println("초기 데이터가 이미 존재.");
            return;
        }

        Member member1 = Member.builder()
                .username("member1")
                .email("qwerty1@gmail.com")
                .password("1234")
                .organization("dept1")
                .deleteStatus(DeleteStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        Member member2 = Member.builder()
                .username("member2")
                .email("qwerty2@gmail.com")
                .password("1234")
                .organization("dept1")
                .deleteStatus(DeleteStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        Member member3 = Member.builder()
                .username("member3")
                .email("qwerty3@gmail.com")
                .password("1234")
                .organization("dept2")
                .deleteStatus(DeleteStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        Member member4 = Member.builder()
                .username("member4")
                .email("qwerty4@gmail.com")
                .password("1234")
                .organization("dept2")
                .deleteStatus(DeleteStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
}
