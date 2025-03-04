package com.example.eroom.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDetail {

    private Long id;
    private String name;
    private List<TagDetail> tags;
}
