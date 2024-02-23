package com.example.YouTube.dto;

import com.example.YouTube.enums.PlaylistStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDTO {
    private Long id;
    private String channelId;
    private String name;
    private String description;
    private PlaylistStatus status;
    private Integer orderNumber;
    private LocalDateTime createdDate;
}
