package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoWatchedDTO {
    private String id;

    @Min(value = 1, message = "profileId should not be less than 1")
    private Integer profileId;

    @Size(min = 36,max = 36, message = "videoId should not be empty")
    @NotBlank(message = "videoId should not be empty")
    private String videoId;

    private LocalDateTime createdDate;
}
