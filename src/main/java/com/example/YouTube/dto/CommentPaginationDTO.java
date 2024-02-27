package com.example.YouTube.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentPaginationDTO {
//    id,content,created_date,like_count,dislike_count, video(id,name,preview_attach_id,title,duration)
    private Integer commentID;
    private String content;
    private LocalDateTime createdDate;
    private Long likeCount;
    private Long dislikeCount;
    private String videoID;
    private String videoName;
    private String previewAttachID;
    private String title;
    private Long duration;

}
