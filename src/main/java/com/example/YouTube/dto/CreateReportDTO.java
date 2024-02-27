package com.example.YouTube.dto;

import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.ReportType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateReportDTO {

    private Integer id;
    private Integer profileID;
    private String content;
    private String entityID;
    private ReportType type;

}
