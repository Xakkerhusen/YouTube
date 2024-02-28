package com.example.YouTube.dto;

import com.example.YouTube.enums.LikeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatedLikeDTO {
    private LikeStatus status;
}
