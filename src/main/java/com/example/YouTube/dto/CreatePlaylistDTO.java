package com.example.YouTube.dto;

import com.example.YouTube.enums.PlaylistStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePlaylistDTO {

    @NotBlank(message = "Channel ID may not be blank")
    private String channelId;

    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotBlank(message = "Description may not be blank")
    private String description;

    @NotNull(message = "Status may not be null")
    private PlaylistStatus status;

    @NotNull(message = "Order number may not be null")
    @PositiveOrZero(message = "Order number must be positive or zero")
    private Integer orderNumber;

}
