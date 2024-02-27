package com.example.YouTube.dto;

import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.entity.VideoEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private Integer id;
    private String content;
    private LocalDateTime createdDate;
    private Long dislikeCount ;
    private Long likeCount ;
    private ProfileEntity profile;
    private Integer profileId;
    private Integer replyId;
    private LocalDateTime updateDate;
    private VideoEntity video;
    private String videoId;
}
