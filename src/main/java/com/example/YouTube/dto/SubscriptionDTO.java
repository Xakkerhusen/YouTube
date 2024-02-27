package com.example.YouTube.dto;

import com.example.YouTube.enums.NotificationType;
import com.example.YouTube.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO {
    private Long id;
    private Integer profileId;
    private String channelId;
    private LocalDateTime createdDate;
    private LocalDateTime unsubscribeDate;
    private Status status;
    private NotificationType notificationType;
}
