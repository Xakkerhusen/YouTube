package com.example.YouTube.dto;

import com.example.YouTube.enums.VideoStatus;
import com.example.YouTube.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoCreateDTO {

    @Min(value = 1, message = "CategoryId should not be less than 1")
    private Integer categoryId;

    @Size(min = 36, max = 36, message = "AttachId must be 36 character")
    @NotBlank(message = "AttachId must be 36 character")
    private String attachId;

    @Size(min = 36, max = 36, message = "ChannelId must be 36 character")
    @NotBlank(message = "ChannelId must be 36 character")
    private String channelId;

    @Size(min = 36, max = 36, message = "PreviewAttachId must be 36 character")
    @NotBlank(message = "PreviewAttachId must be 36 character")
    private String previewAttachId;

    @Size(min = 3, message = "Title should not be empty")
    @NotBlank(message = "Title should not be empty")
    private String title;
    @Size(min = 5, message = "Description should not be empty")
    @NotBlank(message = "Description should not be empty")
    private String description;

    private LocalDateTime createdDate;
    private VideoStatus videoStatus;
    private VideoType videoType;
    private PlaylistDTO playlist;

}
