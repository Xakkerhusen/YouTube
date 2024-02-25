package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoInfoDTO {
    private Integer playlistId;
    private VideoDTO video;
    private ChannelDTO channel;
    private LocalDateTime createdDate;
    private Integer orderNumber;
}
//  playlist_id,video(id,preview_attach(id,url),title,duration),
//            channel(id,name),created_date, order_num