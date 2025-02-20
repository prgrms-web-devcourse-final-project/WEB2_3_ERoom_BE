package com.example.eroom.domain.admin.controller;

import com.example.eroom.domain.admin.dto.request.AdminUpdateMemberDTO;
import com.example.eroom.domain.admin.dto.request.AdminUpdateProjectDTO;
import com.example.eroom.domain.admin.dto.response.AdminMemberDTO;
import com.example.eroom.domain.admin.dto.response.AdminProjectDTO;
import com.example.eroom.domain.admin.service.AdminMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/manage/member")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    public AdminMemberController(AdminMemberService adminMemberService) {
        this.adminMemberService = adminMemberService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminMemberDTO>> memberList() {
        List<AdminMemberDTO> totalMembers = adminMemberService.getTotalMembers();
        return ResponseEntity.ok(totalMembers);
    }

    @PutMapping("/{memberId}/modify")
    public ResponseEntity<AdminMemberDTO> membertModify(
            @PathVariable Long memberId,
            @RequestBody AdminUpdateMemberDTO updateDTO) {

        AdminMemberDTO updatedMember = adminMemberService.updateMember(memberId, updateDTO);

        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{memberId}/delete")
    public ResponseEntity<Void> memberDelete(@PathVariable Long memberId) {
        adminMemberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }


}
