package com.example.YouTube.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoPaginationMapper {
    String getId();

    String getTitle();

    String getPreviewAttachId();

    LocalDateTime getPublishedDate();

    Long getViewCount();

    String getChannelId();

    String getChannelName();

    String getPhotoId();

    Integer getProfileId();

    String getProfileName();

    String getProfileSurname();

    String getPlayListJson();
}
