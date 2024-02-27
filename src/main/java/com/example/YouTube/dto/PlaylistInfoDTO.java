package com.example.YouTube.dto;

import com.example.YouTube.enums.PlaylistStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistInfoDTO {
    private Integer id;
    private String name;
    private String description;
    private PlaylistStatus status;
    private Integer orderNumber;
    private ChannelDTO channel;
    private ProfileDTO profile;


}
//PlayListInfo
//            id,name,description,status(private,public),order_num,
//            channel(id,name,photo(id,url), profile(id,name,surname,photo(id,url)))