package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.response.AdminTagDTO;
import com.example.eroom.domain.admin.service.AdminTagService;
import com.example.eroom.domain.entity.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manage/subcategory/{subcategoryId}/tag")
@RequiredArgsConstructor
public class AdminTagController {
    private final AdminTagService adminTagService;

    @GetMapping("/list")
    public ResponseEntity<List<AdminTagDTO>> tagList(@PathVariable Long subcategoryId) {
        List<AdminTagDTO> tagLists = adminTagService.getTags(subcategoryId);
        return ResponseEntity.ok(tagLists);
    }

    @PostMapping("/create")
    public ResponseEntity<AdminTagDTO> createTag(
            @PathVariable Long subcategoryId,
            @RequestBody AdminTagDTO adminTagDTO) {

        Tag tag = adminTagService.createTag(subcategoryId, adminTagDTO);
        return ResponseEntity.ok(new AdminTagDTO(tag));
    }

    @PutMapping("/{tagId}/modify")
    public ResponseEntity<AdminTagDTO> modifyTag(
            @PathVariable Long tagId,
            @RequestBody AdminTagDTO updateDTO) {
        AdminTagDTO updatedTag = adminTagService.updateTag(tagId, updateDTO);

        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{tagId}/delete")
    public ResponseEntity<Void> tagDelete(@PathVariable Long tagId) {
        try {
            adminTagService.deleteTag(tagId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}
