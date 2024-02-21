package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagNameDTO {
    private Integer id;
    @NotBlank(message = "Tag name can not be null or blank")
    private String tagName;
    private LocalDateTime createdDate;
}
