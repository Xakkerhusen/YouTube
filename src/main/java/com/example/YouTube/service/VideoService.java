package com.example.YouTube.service;

import com.example.YouTube.dto.VideoCreateDTO;
import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;


    public VideoCreateDTO create(VideoCreateDTO dto) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setChannelId(dto.getChannelId());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setVideoStatus(dto.getVideoStatus());
        entity.setVideoType(dto.getVideoType());

        videoRepository.save(entity);
        dto.setCreatedDate(LocalDateTime.now());

        return dto;
    }

}
