package com.example.YouTube.mapper;

import com.example.YouTube.enums.LikeStatus;

import java.time.LocalDateTime;

public interface VideoFullInfoMapper {
    String getVideoId();

    String getTitle();

    String getPreviewAttachId();

    String getAttachId();

    String getChannelId();

    String getPhotoId();

    Integer getCategoryId();

    LocalDateTime getPublishedDate();

    Long getViewCount();

    String getChannelName();

    String getDescription();

    Long getSharedCount();

    Long getLikeCount();

    Long getDislikeCount();

    Long getDuration();

    String getCategoryName();

    String getUrl();

    String getTagListJson();

    LikeStatus getEmotion();
}
