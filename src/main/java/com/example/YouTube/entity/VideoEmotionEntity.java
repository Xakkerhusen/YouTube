package com.example.YouTube.entity;

import com.example.YouTube.enums.LikeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "video_emotion")
public class VideoEmotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "video_id", nullable = false)
    private String videoId;
    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false, insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "profile_id", nullable = false)
    private Integer profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "created_date")
    @CreationTimestamp
    protected LocalDateTime createdDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private LikeStatus status;

    @Column(name = "visible")
    private Boolean visible = true;

}
