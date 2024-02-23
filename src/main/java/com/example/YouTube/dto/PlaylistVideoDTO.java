package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO {
    private Long id;

    private Long playlistId;

    private String videoId;

    private LocalDateTime createdDate;

    private Integer orderNumber;
}
