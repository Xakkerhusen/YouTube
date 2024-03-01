package com.example.YouTube.mapper;

import java.time.LocalDateTime;

public interface PlayListShortInfoMapper {
    Integer getPlaylistId();
    String getPlaylistName();
    LocalDateTime getPlaylistCreatedDate();
    Integer getPlaylistVideoCount();
    String getChannelId();
    String getChannelName();
    String getPlayListJson();
}
