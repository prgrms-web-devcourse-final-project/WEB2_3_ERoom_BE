package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.response.AdminCategoryDTO;
import com.example.eroom.domain.admin.service.AdminCategoryService;
import com.example.eroom.domain.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manage/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @GetMapping("/list")
    public ResponseEntity<List<AdminCategoryDTO>> categoryList() {
        List<AdminCategoryDTO> lists = adminCategoryService.getCategoryLists();
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<AdminCategoryDTO> categoryListByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(adminCategoryService.getCategoryById(categoryId));
    }

    @PostMapping("/create")
    public ResponseEntity<AdminCategoryDTO> createCategory(@RequestBody AdminCategoryDTO adminCategoryDTO) {
        Category category = adminCategoryService.createCategory(adminCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminCategoryDTO(category));
    }

    @PutMapping("/{categoryId}/modify")
    public ResponseEntity<AdminCategoryDTO> categoryModify(@PathVariable Long categoryId, @RequestBody AdminCategoryDTO updateDTO) {
        AdminCategoryDTO updatedCategory = adminCategoryService.updateCategory(categoryId, updateDTO);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<AdminCategoryDTO> categoryDelete(@PathVariable Long categoryId) {
        try {
            adminCategoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }


}
