package com.example.YouTube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "content",columnDefinition = "TEXT")
    private String content;


    @Column(name = "created_date")
    private LocalDateTime createdDate;


    @Column(name = "dislike_count")
    private Long dislikeCount=0l;


    @Column(name = "like_count")
    private Long likeCount=0l;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "reply_id")
    private Integer replyId=0;

    @Column(name = "update_date")
    private LocalDateTime updateDate;



    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "video_id")
    private String videoId;

}
