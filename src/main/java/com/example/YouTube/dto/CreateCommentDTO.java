package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCommentDTO {
    private String id;
    private String content;
    private LocalDateTime createdDate;
    private Integer profileID;
    private String videoID;
    private String replyID;


}
