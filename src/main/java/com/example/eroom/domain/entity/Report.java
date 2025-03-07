package com.example.eroom.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Report {

    @Builder
    Report(ChatRoom chatRoom, String content, String title, LocalDateTime startDate, LocalDateTime endDate, DeleteStatus status, String members){
        this.chatRoom = chatRoom;
        this.content = content;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleteStatus = status;
        this.members = members;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String title;

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus; // ACTIVE, DELETED

    private String members;

}
