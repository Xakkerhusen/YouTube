package com.example.YouTube.entity;

import com.example.YouTube.enums.VideoStatus;
import com.example.YouTube.enums.VideoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private CategoryEntity category;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false, insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "preview_attach_id", nullable = false)
    private String previewAttachId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false, insertable = false, updatable = false)
    private AttachEntity previewAttach;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "video_status")
    private VideoStatus videoStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "video_type")
    private VideoType videoType;

    @Column(name = "duration")
    private Long duration = 0l;
    @Column(name = "view_count")
    private Long viewCount = 0l;
    @Column(name = "shared_count")
    private Long sharedCount = 0l;
    @Column(name = "like_count")
    private Long likeCount = 0l;
    @Column(name = "dislike_count")
    private Long dislikeCount = 0l;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    private List<PlaylistVideoEntity> playlistVideoEntityList;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    private List<VideoTagEntity> videoTagEntityList;

}


//        id(uuid), preview_attach_id,title,category_id,attach_id,created_date,published_date,
//        status(private,public),
//        type(video,short),view_count,shared_count,description,channel_id,(like_count,dislike_count),

//        view_count -> Okala view_count buyerda ham bo'lsin. Alohida Table ham bo'lsin.
//        category_id -> bitta video bitta category bo'lsin.