package com.example.eroom.domain.chat.dto.request;

import com.example.eroom.domain.entity.Member;
import com.example.eroom.domain.entity.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDTO {
    private Member recipient;
    private String message;
    private NotificationType type;
    private Long referenceId;
    private String referenceName;
}