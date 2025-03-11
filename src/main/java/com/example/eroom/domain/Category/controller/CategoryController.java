package com.example.eroom.domain.Category.controller;

import com.example.eroom.domain.Category.dto.response.CategoryDTO;
import com.example.eroom.domain.Category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> categoryList() {
        List<CategoryDTO> lists = categoryService.getCategoryLists();
        return ResponseEntity.ok(lists);
    }
}
