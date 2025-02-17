package com.example.eroom.domain.chat.interceptor;

import com.example.eroom.domain.chat.thymeleaf.service.ProjectServiceEx;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ProjectAccessInterceptor implements HandlerInterceptor {

    private final ProjectServiceEx projectServiceEx;

    public ProjectAccessInterceptor(ProjectServiceEx projectServiceEx) {
        this.projectServiceEx = projectServiceEx;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/project/")) {
            String[] uriParts = uri.split("/");
            if (uriParts.length > 2) {
                Long projectId = Long.parseLong(uriParts[2]);
                Member currentMember = (Member) request.getSession().getAttribute("member");

                // 사용자가 해당 프로젝트의 멤버인지 확인
                if (currentMember == null || !projectServiceEx.isUserMemberOfProject(currentMember, projectId)) {
                    response.sendRedirect("/project/list");
                    return false;
                }
            }
        }
        return true;
    }
}
