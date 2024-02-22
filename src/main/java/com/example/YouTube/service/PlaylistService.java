package com.example.YouTube.service;

import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.CreatePlaylistDTO;
import com.example.YouTube.dto.PlaylistDTO;
import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ChannelRepository;
import com.example.YouTube.repository.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PlaylistService {
    /**
     * This object is used to work with the database
     */
    @Autowired
    private PlaylistRepository playlistRepository;
    /**
     * This object is used to get channel by its id
     */
    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ResourceBundleService resourceBundleService;

    /**
     * This method is used to create a new playlist
     */
    public String create(CreatePlaylistDTO dto, AppLanguage language) {
        ChannelDTO channelDTO = channelService.getById(dto.getChannelId(),language);

        PlaylistEntity entity=new PlaylistEntity();

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setChannelId(channelDTO.getId());
        entity.setCreatedDate(LocalDateTime.now());

        playlistRepository.save(entity);

        return "Playlist has created successfully";
    }

    /**
     * This method is used to update a new playlist
     */
    public String update(Long playlistId, Integer profileId, PlaylistDTO dto, AppLanguage language){
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(),language);

        if(!profileId.equals(channelDTO.getProfileId())){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.found", language)+"-->>"+profileId);

        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());

        playlistRepository.save(entity);
        return "Playlist has updated successfully";

    }


    private PlaylistEntity get(Long playlistId, AppLanguage language) {
        return playlistRepository.findById(playlistId).orElseThrow(() -> {
            log.warn("Profile not found{}", playlistId);
            return new AppBadException(resourceBundleService.getMessage("playlist.not.found", language)+"-->"+playlistId);
        });

    }





}
