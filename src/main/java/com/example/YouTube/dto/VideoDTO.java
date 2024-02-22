package com.example.YouTube.dto;

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
    private Integer categoryId;
    private String attachId;
    private String channelId;
    private String previewAttachId;
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
}
