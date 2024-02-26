package com.example.YouTube.entity;

import com.example.YouTube.enums.NotificationType;
import com.example.YouTube.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "unsubscribe_date")
    private LocalDateTime unsubscribeDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

}
