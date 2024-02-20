package com.example.YouTube.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelImageDTO {
    @NotBlank(message = "ImageId can not empty")
    private String imageId;
}
