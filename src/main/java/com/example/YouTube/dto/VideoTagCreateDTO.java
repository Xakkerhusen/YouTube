package com.example.YouTube.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoTagCreateDTO {
    @NotBlank(message = "videoId can not be null or blank")
    private String videoId;
    private Integer tagId;
}
