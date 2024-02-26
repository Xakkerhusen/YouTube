package com.example.YouTube.dto;

import com.example.YouTube.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoEmotionDTO {

    private Integer id;

    @Size(min = 36,max = 36, message = "videoId should not be empty")
    @NotBlank(message = "videoId should not be empty")
    private String videoId;

    private VideoDTO video;

    @Min(value = 1, message = "profileId should not be less than 1")
    private Integer profileId;

    private LocalDateTime createdDate;

    private LikeStatus status;

}
