package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoTagDTO {
    private Integer id;
    private String videoId;
    private TagNameDTO tag;
    private LocalDateTime createdDate;
}
