package com.example.eroom.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int count = 0;

    @ManyToOne
    @JoinColumn(name = "sub_category_id", nullable = false)
    @JsonBackReference // 순환 참조 방지
    private SubCategory subCategory;
}
