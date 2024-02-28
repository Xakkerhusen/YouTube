package com.example.YouTube.mapper;

import java.time.LocalDateTime;

public interface VideoShortInfoMapper {

    String getId();

    String getTitle();

    String getPreviewAttachId();

    LocalDateTime getPublishedDate();

    Long getViewCount();

    Long getDuration();

    String getChannelId();

    String getChannelName();

    String getPhotoId();

}
