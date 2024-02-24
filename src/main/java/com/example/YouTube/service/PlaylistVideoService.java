package com.example.YouTube.service;

import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.PlaylistVideoDTO;
import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PlaylistVideoService {

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private VideoService  videoService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ResourceBundleService resourceBundleService;
    public String create(Integer profileId, PlaylistVideoDTO dto, AppLanguage language){
        PlaylistVideoEntity entity=new PlaylistVideoEntity();

        PlaylistEntity playlist = playlistService.get(dto.getPlaylistId(), language);
        ChannelDTO channel = channelService.getById(playlist.getChannelId(), language);

        if(!channel.getProfileId().equals(profileId)){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language)+"-->>"+profileId);
        }

        VideoEntity videoEntity = videoService.get(dto.getVideoId(), language);

        entity.setOrderNumber(dto.getOrderNumber());
        entity.setPlaylistId(playlist.getId());
        entity.setVideoId(videoEntity.getId());
        entity.setCreatedDate(LocalDateTime.now());

        playlistVideoRepository.save(entity);

        return "Build success";

    }




}
