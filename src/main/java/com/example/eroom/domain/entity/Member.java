package com.example.eroom.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLDelete(sql = "UPDATE member SET delete_status = 'DELETED' WHERE id = ?")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String organization;
    private MemberGrade memberGrade;
    private String profile;
    private LocalDate createdAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status", nullable = false)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVE; // ACTIVE, DELETED

    // 이름 수정 메서드
    public void updateUserName(String userName) {
        this.username = userName;
    }

    // 상태 수정 메서드
    public void activateUser() {
        this.deleteStatus = DeleteStatus.ACTIVE;
    }
}
