package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistDetailInfoDTO {
    private Integer id;
    private String name;
    private Integer videoCount;
    private Integer totalViewCount;
    private LocalDateTime lastUpdateDate;
}
