package com.example.eroom.domain.chat.interceptor;

import com.example.eroom.domain.chat.service.ProjectService;
import com.example.eroom.domain.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ProjectAccessInterceptor implements HandlerInterceptor {

    private final ProjectService projectService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/projects/")) {
            String[] uriParts = uri.split("/");
            if (uriParts.length > 3) {
                Long projectId = Long.parseLong(uriParts[3]);
                Member currentMember = (Member) request.getSession().getAttribute("member");

                // 사용자가 해당 프로젝트의 멤버인지 확인
                if (currentMember == null || !projectService.isMemberOfProject(currentMember, projectId)) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("Access Denied");
                    return false;
                }
            }
        }
        return true;
    }
}
