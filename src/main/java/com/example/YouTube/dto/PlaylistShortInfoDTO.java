package com.example.YouTube.dto;

import com.example.YouTube.entity.VideoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistShortInfoDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
    private ChannelDTO channel;
    private  Integer videoCount;
    private List<VideoDTO> videos;


}
//PlayListShortInfo
//            id, name,created_date,channel(id,name),video_count,video_list[{id,name,duration}] (first 2)