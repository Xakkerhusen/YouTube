package com.example.YouTube.mapper;

import lombok.Getter;
import lombok.Setter;

public interface VideolikeInfoMapper {
    /*id,video(id,name,channel(id,name),duration),preview_attach(id,url)*/
    Integer getVideoLikeId();

    String getVideoId();

    String getVideoName();

    String getChannalId();

    String getChannalName();

    Long getDuration();

    String getPreviewAttachId();

}
