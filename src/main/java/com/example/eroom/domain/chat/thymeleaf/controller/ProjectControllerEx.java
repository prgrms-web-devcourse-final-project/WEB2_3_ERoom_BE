package com.example.eroom.domain.chat.thymeleaf.controller;

import com.example.eroom.domain.chat.thymeleaf.service.ProjectServiceEx;
import com.example.eroom.domain.chat.thymeleaf.service.TaskService;
import com.example.eroom.domain.chat.thymeleaf.service.MemberService;
import com.example.eroom.domain.entity.Project;
import com.example.eroom.domain.entity.Task;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectControllerEx {

    private final ProjectServiceEx projectServiceEx;
    private final MemberService memberService;
    private final TaskService taskService;

    // 프로젝트 목록 보기
    @GetMapping("/list")
    public String listProjects(Model model, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return "redirect:/member/login";
        }

        List<Project> projects = projectServiceEx.getProjectsByUser(currentMember);
        model.addAttribute("projects", projects);
        return "project/list";
    }

    // 프로젝트 생성 폼
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return "redirect:/member/login";
        }

        model.addAttribute("project", new Project());
        model.addAttribute("members", memberService.getAllUsersExcept(currentMember));
        return "project/create";
    }

    // 프로젝트 생성 처리
    @PostMapping("/create")
    public String createProject(@ModelAttribute Project project,
                                @RequestParam List<Long> invitedMemberIds,
                                HttpSession session) {
        Member creator = (Member) session.getAttribute("member");
        if (creator == null) {
            return "redirect:/member/login";
        }

        projectServiceEx.createProject(project, creator, invitedMemberIds);
        return "redirect:/project/list";
    }

    // 프로젝트 상세 보기
    @GetMapping("/{projectId}")
    public String viewProjectDetail(@PathVariable Long projectId, Model model, HttpSession session) {
        Member currentMember = (Member) session.getAttribute("member");
        if (currentMember == null) {
            return "redirect:/member/login";
        }

//        Project project = projectService.getProjectById(projectId);
//        model.addAttribute("project", project);

        Project project = projectServiceEx.getProjectById(projectId);
        List<Task> tasks = taskService.getTasksByProject(project);
        List<Member> participants = projectServiceEx.getProjectParticipantsExceptCurrentMember(project, currentMember);

        model.addAttribute("project", project);
        model.addAttribute("tasks", tasks);
        model.addAttribute("participants", participants);

        return "project/detail";
    }

    // task 생성
    @PostMapping("/{projectId}/task/create")
    public String addTaskToProject(@PathVariable Long projectId,
                                   @RequestParam String title,
                                   @RequestParam String description,
                                   @RequestParam(required = false) Long assignedUserId,
                                   @RequestParam String startDate,
                                   @RequestParam String endDate) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T23:59:59");

        taskService.addTaskToProject(projectId, title, description, assignedUserId, startDateTime, endDateTime);
        return "redirect:/project/" + projectId;
    }

}
