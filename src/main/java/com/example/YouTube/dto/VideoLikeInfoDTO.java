package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoLikeInfoDTO {

    private Integer videoLikeId;
    private VideoDTO video;
    private ChannelDTO channel;
    private AttachDTO previewAttach;
    private Long duration;
}
