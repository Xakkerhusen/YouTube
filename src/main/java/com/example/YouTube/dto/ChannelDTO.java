package com.example.YouTube.dto;

import com.example.YouTube.enums.Status;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ChannelDTO {
    private String id;
    private String name;
    private String photoId;
    private String description;
    private Status status;
    private String bannerId;
    private Integer profileId;
}
