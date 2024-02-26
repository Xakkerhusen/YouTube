package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistVideoDTO {
    private Long id;

    @NotNull(message = "Playlist ID must not be null")
    private Integer playlistId;

    @NotNull(message = "Video ID must not be null")
    private String videoId;

    @NotNull(message = "Order number must not be null")
    @PositiveOrZero(message = "Order number must be positive or zero")
    private Integer orderNumber;

    private LocalDateTime createdDate;
}
