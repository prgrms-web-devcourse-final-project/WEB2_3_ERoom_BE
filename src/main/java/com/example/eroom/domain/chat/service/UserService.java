package com.example.eroom.domain.chat.service;

import com.example.eroom.domain.chat.repository.UserRepository;
import com.example.eroom.domain.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(Member member) {
        userRepository.save(member);
    }

    public Member login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    // 현재 유저를 제외한 모든 유저 목록 가져오기
    public List<Member> getAllUsersExcept(Member currentMember) {
        return userRepository.findAllByIdNot(currentMember.getId());
    }

    public List<Member> getAllUsers() {
        return userRepository.findAll();
    }
}
