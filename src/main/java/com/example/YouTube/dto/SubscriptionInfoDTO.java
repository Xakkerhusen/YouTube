package com.example.YouTube.dto;

import com.example.YouTube.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionInfoDTO {
    private Long id;
    private ChannelDTO channel;
    private NotificationType notificationType;
    private LocalDateTime createdDate;
}
