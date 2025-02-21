package com.example.eroom.domain.admin.repository;

import com.example.eroom.domain.entity.DeleteStatus;
import com.example.eroom.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMemberJPARepository extends JpaRepository<Member, Long> {
    List<Member> findByDeleteStatus(DeleteStatus deleteStatus);
}
