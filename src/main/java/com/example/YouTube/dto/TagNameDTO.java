package com.example.YouTube.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class TagNameDTO {
    private Integer id;
    private String tagName;
    private LocalDateTime createdDate;
}
