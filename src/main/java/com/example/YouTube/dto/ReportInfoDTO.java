package com.example.YouTube.dto;

import com.example.YouTube.enums.ReportType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportInfoDTO {
//    RepostInfo
//    id,profile(id,name,surname,photo(id,url)),content,
//    entity_id(channel/video)),type(channel,video)

    private Integer reportID;
    private Integer profileID;
    private String profileName;
    private String profileSurname;
    private String profilePhotoID;
    private String content;
    private String entityID;
    private ReportType type;

}
