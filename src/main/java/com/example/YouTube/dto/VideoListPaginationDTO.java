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
public class VideoListPaginationDTO {
    private String id;
    private String title;
    private String description;
    private Long duration;
    private CategoryDTO category;
    private AttachDTO attach;
    private AttachDTO previewAttach;
    private ChannelDTO channel;
    private VideoStatus status;
    private VideoType type;
    private Long viewCount;
    private Long sharedCount;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private ProfileDTO owner;
    private String playListJson;
}
