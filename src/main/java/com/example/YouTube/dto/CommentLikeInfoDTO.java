package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.bytedeco.opencv.presets.opencv_core;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeInfoDTO {
    private Integer id;
    private Integer profileId;
    private Integer commentId;
    private LocalDateTime createdDate;
    private String type;
}
