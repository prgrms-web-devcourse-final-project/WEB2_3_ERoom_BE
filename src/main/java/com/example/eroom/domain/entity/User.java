package com.example.eroom.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String organization;
    private UserGrade userGrade;
    private String profile;

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus = DeleteStatus.ACTIVE; // ACTIVE, DELETED
}
