package com.example.YouTube.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatChannelDTO {
    @NotBlank(message = "Name can not be null or blank")
    private String name;
    @NotBlank(message = "PhotoId can not be null or blank")
    private String photoId;
    @NotBlank(message = "Description can not be null or blank")
    private String description;
    @NotBlank(message = "BannerId can not be null or blank")
    private String bannerId;
}
