package com.example.eroom.domain.Category.controller;

import com.example.eroom.domain.Category.dto.response.TagDTO;
import com.example.eroom.domain.Category.service.TagService;
import com.example.eroom.domain.admin.dto.response.AdminTagDTO;
import com.example.eroom.domain.admin.service.AdminTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subcategory/{subcategoryId}/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public ResponseEntity<List<TagDTO>> tagList(@PathVariable Long subcategoryId) {
        List<TagDTO> tagLists = tagService.getTags(subcategoryId);
        return ResponseEntity.ok(tagLists);
    }
}
