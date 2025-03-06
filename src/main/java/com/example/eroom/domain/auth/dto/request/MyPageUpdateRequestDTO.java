package com.example.eroom.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class MyPageUpdateRequestDTO {
    @NotBlank
    private String username;
    private String organization;
    private MultipartFile profileImage;

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setProfileImage(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }
}

