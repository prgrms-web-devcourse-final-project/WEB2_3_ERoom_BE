package com.example.eroom.domain.Category.dto.response;

import com.example.eroom.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    private Long id;
    private String name;
    private int count;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
    }
}