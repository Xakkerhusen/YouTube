package com.example.YouTube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "video_like_history")
public class VideoLikeHistoryEntity extends BaseEntity{
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne()
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;
    @Column(name = "video_id")
    private String videoId;
    @ManyToOne()
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;
    @Column(name = "type")
    private String type;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;
}
