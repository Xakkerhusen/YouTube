package com.example.YouTube.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterEmailHistoryDTO {
    private String email;
    private LocalDate fromDate;
    private LocalDate toDate;
}
