package com.example.eroom.domain.Category.controller;

import com.example.eroom.domain.Category.dto.response.SubCategoryDTO;
import com.example.eroom.domain.Category.service.SubCategoryService;
import com.example.eroom.domain.admin.dto.response.AdminSubCategoryDTO;
import com.example.eroom.domain.admin.service.AdminSubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subcategory")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/list")
    public ResponseEntity<List<SubCategoryDTO>> subCategoryList(@RequestParam Long categoryId) {
        List<SubCategoryDTO> subCategoryLists = subCategoryService.getSubCategories(categoryId);
        return ResponseEntity.ok(subCategoryLists);
    }
}
