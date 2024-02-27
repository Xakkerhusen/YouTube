package com.example.YouTube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "video_like")
public class VideoLikeEntity extends BaseEntity{
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne()
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;
    @Column(name = "comment_id")
    private String commentId;
    @ManyToOne()
    @JoinColumn(name = "comment_id",insertable = false,updatable = false)
    private CommentEntity comment;
    @Column(name = "type")
    private String type;
    @Column(name = "createdDate")
    private LocalDateTime createdDate=LocalDateTime.now();
}
