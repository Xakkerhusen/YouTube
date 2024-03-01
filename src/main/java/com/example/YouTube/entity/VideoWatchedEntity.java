package com.example.YouTube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "video_watch")
public class VideoWatchedEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
