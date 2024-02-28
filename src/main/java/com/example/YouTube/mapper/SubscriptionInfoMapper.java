package com.example.YouTube.mapper;

import com.example.YouTube.enums.NotificationType;

import java.time.LocalDateTime;

public interface SubscriptionInfoMapper {
    Long getSubscriptionsId();
    String getChannelId();
    String getChannelPhotoId();
    NotificationType getSubscriptionsNotificationType();
    LocalDateTime getSubscriptionCreatedDate();
}
