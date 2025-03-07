package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.response.AdminSubCategoryDTO;
import com.example.eroom.domain.admin.service.AdminSubCategoryService;
import com.example.eroom.domain.entity.SubCategory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manage/subcategory")
@RequiredArgsConstructor
public class AdminSubCategoryController {
    private final AdminSubCategoryService adminSubCategoryService;

    @GetMapping("/list")
    public ResponseEntity<List<AdminSubCategoryDTO>> subCategoryList(@RequestParam Long categoryId) {
        List<AdminSubCategoryDTO> subCategoryLists = adminSubCategoryService.getSubCategories(categoryId);
        return ResponseEntity.ok(subCategoryLists);
    }

    @PostMapping("/{categoryId}/create")
    public ResponseEntity<AdminSubCategoryDTO> createSubCategory(
            @PathVariable Long categoryId,
            @RequestBody AdminSubCategoryDTO adminSubCategoryDTO) {
        SubCategory subCategory = adminSubCategoryService.createSubCategory(categoryId, adminSubCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminSubCategoryDTO(subCategory));
    }

    @PutMapping("/{subcategoryId}/modify")
    public ResponseEntity<AdminSubCategoryDTO> modifySubCategory(
            @PathVariable Long subcategoryId,
            @RequestBody AdminSubCategoryDTO updateDTO) {
        AdminSubCategoryDTO updatedSubCategory = adminSubCategoryService.updateSubCategory(subcategoryId, updateDTO);

        return ResponseEntity.ok(updatedSubCategory);
    }

    @DeleteMapping("/{subcategoryId}/delete")
    public ResponseEntity<Void> subCategoryDelete(@PathVariable Long subcategoryId) {
        try {
            adminSubCategoryService.deleteSubCategory(subcategoryId);
            return ResponseEntity.noContent().build(); // 204 No content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
