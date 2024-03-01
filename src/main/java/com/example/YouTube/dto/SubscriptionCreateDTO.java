package com.example.YouTube.dto;

import com.example.YouTube.enums.NotificationType;
import com.example.YouTube.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionCreateDTO {
    private String channelId;
    private NotificationType notificationType;
    private Status status;
}
