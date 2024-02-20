package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {

    private String id;

    private String originalName;

    private Long size;

    private String extension;

    @NotBlank(message = "Path field must have a value")
    private String path;

    private String url;

    private long duration;

    private LocalDateTime createdData;
}
