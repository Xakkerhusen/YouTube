package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ChannelRepository;
import com.example.YouTube.repository.PlaylistRepository;
import com.example.YouTube.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    @Autowired
    private VideoService videoService;

    /**
     * This method is used to create a new playlist
     */
    public String create(Integer profileId, CreatePlaylistDTO dto, AppLanguage language) {
        ChannelDTO channelDTO = channelService.getById(dto.getChannelId(),language);

        if(!profileId.equals(channelDTO.getProfileId())){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language)+"-->>"+profileId);

        }
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
    public String update(Integer playlistId, Integer profileId, PlaylistDTO dto, AppLanguage language){
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(),language);

        if(!profileId.equals(channelDTO.getProfileId())){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language)+"-->>"+profileId);

        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());

        playlistRepository.save(entity);
        return "Playlist has updated successfully";

    }

    public String updateStatus(Integer playlistId, Integer profileId, PlaylistDTO dto, AppLanguage language){
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(),language);

        if(!profileId.equals(channelDTO.getProfileId())){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language)+"-->>"+profileId);

        }
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());

        playlistRepository.save(entity);
        return "Playlist status has updated successfully";

    }

    public Boolean delete(Integer playlistId, Integer profileId, AppLanguage language){
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(),language);

        if(!profileId.equals(channelDTO.getProfileId())){
            log.warn("Profile not found{}", profileId);
            throw  new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language)+"-->>"+profileId);

        }

        playlistRepository.delete(entity.getId());

        return true;
    }

    public PageImpl<PlaylistInfoDTO> pagination(Integer page, Integer size, AppLanguage language) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<PlaylistEntity> pages = playlistRepository.findAll(pageable);

        List<PlaylistEntity> content = pages.getContent();
        long totalElements = pages.getTotalElements();

        List<PlaylistInfoDTO> dtoList=new ArrayList<>();
        for (PlaylistEntity entity : content) {
            dtoList.add(toDTO(entity,language));
        }

        return new PageImpl<>(dtoList,pageable,totalElements);

    }

    public List<PlaylistShortInfoDTO> getPlaylistByUser(Integer profileId,AppLanguage language){

        List<PlaylistEntity> playlistEntities = playlistRepository.find();

        List<PlaylistShortInfoDTO> dtoList = new LinkedList<>();

        for (PlaylistEntity playlistEntity : playlistEntities) {
            ChannelEntity channelEntity = channelService.get(playlistEntity.getChannelId(), language);
            if (channelEntity != null && channelEntity.getProfileId().equals(profileId)) {
                dtoList.add(toShortDTO(playlistEntity, language));
            }
        }

        return dtoList;


    }

    public PlaylistInfoDTO toDTO(PlaylistEntity entity, AppLanguage language){

        PlaylistInfoDTO dto=new PlaylistInfoDTO();
        ChannelDTO channel = channelService.getById(entity.getChannelId(), language);
        ProfileEntity profile = profileService.get(channel.getProfileId(), language);
        AttachEntity attachChannel = attachService.get(channel.getPhotoId(), language);
        AttachEntity attachProfile = attachService.get(profile.getAttachId(), language);

        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(channel.getId());
        channelDTO.setName(channel.getName());
        AttachDTO channelPhoto=new AttachDTO();
        channelPhoto.setId(attachChannel.getId());
        channelPhoto.setUrl(attachChannel.getUrl());
        channelDTO.setPhoto(channelPhoto);

        ProfileDTO profileDTO=new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setName(profile.getName());
        profileDTO.setSurname(profile.getSurname());
        AttachDTO profilePhoto=new AttachDTO();
        profilePhoto.setId(attachProfile.getId());
        profilePhoto.setUrl(attachProfile.getUrl());
        profileDTO.setAttach(profilePhoto);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setChannel(channelDTO);
        dto.setProfile(profileDTO);


        return dto;
    }

    public PlaylistShortInfoDTO toShortDTO(PlaylistEntity entity, AppLanguage language){
        PlaylistShortInfoDTO dto=new PlaylistShortInfoDTO();

        Iterable<PlaylistVideoEntity> all = playlistVideoRepository.findAll();
        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
        List<VideoDTO> dtoList=new LinkedList<>();
        for (PlaylistVideoEntity playlistVideoEntity : all) {
            VideoDTO videoDTO=new VideoDTO();
            VideoEntity videoEntity = videoService.get(playlistVideoEntity.getVideoId(), language);
            videoDTO.setId(videoEntity.getId());
            videoDTO.setTitle(videoEntity.getTitle());
            videoDTO.setDuration(videoEntity.getDuration());
            dtoList.add(videoDTO);
        }

        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoCount(entity.getVideoCount());
        dto.setChannel(channelDTO);
        dto.setVideos(dtoList);

        return dto;
    }


    public PlaylistEntity get(Integer playlistId, AppLanguage language) {
        return playlistRepository.findById(playlistId).orElseThrow(() -> {
            log.warn("Profile not found{}", playlistId);
            return new AppBadException(resourceBundleService.getMessage("playlist.not.found", language)+"-->"+playlistId);
        });

    }
}
