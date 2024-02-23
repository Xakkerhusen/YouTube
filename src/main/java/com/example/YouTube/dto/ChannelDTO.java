package com.example.YouTube.dto;

import com.example.YouTube.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO {
    private String id;
    private String name;
    private String photoId;
    private AttachDTO photo;
    private String description;
    private Status status;
    private String bannerId;
    private Integer profileId;
}
