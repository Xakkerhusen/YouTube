package com.example.YouTube.controller;

import com.example.YouTube.enums.NotificationType;
import com.example.YouTube.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeSubscriptionNotificationDTO {
    private String channelId;
    private NotificationType notificationType;
}
