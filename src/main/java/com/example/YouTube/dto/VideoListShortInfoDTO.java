package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoListShortInfoDTO {
    private String id;
    private String title;
    private Long viewCount;
    private Long duration;
    private LocalDateTime publishedDate;
    private AttachDTO previewAttach;
    private ChannelDTO channel;
    private AttachDTO attach;
}
