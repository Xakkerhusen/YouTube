package com.example.YouTube.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfoMapper {
    Integer getPlaylistId();
    String getVideoId();
    String getVideoPreviewAttachId();
    String getVideoTitle();
    Long getVideoDuration();
    String getChannelId();
    String getChannelName();
    LocalDateTime getPlaylistVideoCreatedDate();
    Integer getPlaylistVideoOrderNumber();
}
