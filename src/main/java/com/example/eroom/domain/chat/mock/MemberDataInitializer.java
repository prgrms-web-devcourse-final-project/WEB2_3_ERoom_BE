package com.example.eroom.domain.chat.mock;

import com.example.eroom.domain.chat.repository.MemberRepository;
import com.example.eroom.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {

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
    }
}
