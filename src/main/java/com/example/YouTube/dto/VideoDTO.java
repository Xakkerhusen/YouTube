package com.example.YouTube.dto;

import com.example.YouTube.enums.LikeStatus;
import com.example.YouTube.enums.VideoStatus;
import com.example.YouTube.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    private String id;
    private ProfileDTO profile;
    private Integer categoryId;
    private CategoryDTO category;
    private String attachId;
    private AttachDTO attach;
    private String channelId;
    private ChannelDTO channel;
    private String previewAttachId;
    private AttachDTO previewAttach;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private VideoStatus videoStatus;
    private VideoType videoType;
    private Long viewCount;
    private Long sharedCount;
    private Long likeCount;
    private Long dislikeCount;
    private long duration;
    private PlaylistDTO playlist;
}
