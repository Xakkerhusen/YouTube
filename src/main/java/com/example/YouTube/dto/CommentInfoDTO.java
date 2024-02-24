package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentInfoDTO {
    private String commentID;
    private String content;
    private LocalDateTime createdDate;
    private Long likeCount;
    private Long dislikeCount;
    private Integer profileID;
    private String profileName;
    private String profileSurname;
    private String profilePhotoId;
}
