package com.example.eroom.domain.admin.dto.response;

import com.example.eroom.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTagDTO {
    private Long id;
    private String name;

    public AdminTagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();

    }
}
