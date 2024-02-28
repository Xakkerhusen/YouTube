package com.example.YouTube.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoPaginationMapper {
    public String getId();
    public String getTitle();
    public String getPreviewAttachId();
    public LocalDateTime getPublishedDate();
    public Long getViewCount();
    public String getChannelId();
    public String getChannelName();
    public String getPhotoId();
    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getPlayListJson();
}
