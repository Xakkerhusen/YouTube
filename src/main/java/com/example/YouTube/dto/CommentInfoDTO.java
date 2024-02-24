package com.example.YouTube.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentInfoDTO {
    private Integer commentID;
    private String content;
    private LocalDateTime createdDate;
    private Long likeCount;
    private Long dislikeCount;
    private Integer profileID;
    private String profileName;
    private String profileSurname;
    private String profilePhotoId;
}
