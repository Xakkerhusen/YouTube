package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.mapper.PlaylistVideoInfoMapper;
import com.example.YouTube.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PlaylistVideoService {

    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlaylistService playlistService;
    private final VideoService videoService;
    private final ChannelService channelService;
    private final ResourceBundleService resourceBundleService;
    private final AttachService attachService;
    @Autowired
    public PlaylistVideoService(PlaylistVideoRepository playlistVideoRepository, PlaylistService playlistService,
                                VideoService videoService, ChannelService channelService,
                                ResourceBundleService resourceBundleService, AttachService attachService) {
        this.playlistVideoRepository = playlistVideoRepository;
        this.playlistService = playlistService;
        this.videoService = videoService;
        this.channelService = channelService;
        this.resourceBundleService = resourceBundleService;
        this.attachService = attachService;
    }

    public String create(Integer profileId, PlaylistVideoDTO dto, AppLanguage language) {
        PlaylistVideoEntity entity = new PlaylistVideoEntity();

        PlaylistEntity playlist = playlistService.get(dto.getPlaylistId(), language);
        ChannelDTO channel = channelService.getById(playlist.getChannelId(), language);

        if (!channel.getProfileId().equals(profileId)) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);
        }

        VideoEntity videoEntity = videoService.get(dto.getVideoId(), language);

        entity.setOrderNumber(dto.getOrderNumber());
        entity.setPlaylistId(playlist.getId());
        entity.setVideoId(videoEntity.getId());
        entity.setCreatedDate(LocalDateTime.now());

        playlistVideoRepository.save(entity);

        return "Build success";

    }

    public String update(Integer profileId, PlaylistVideoDTO dto, AppLanguage language) {
        PlaylistEntity playlist = playlistService.get(dto.getPlaylistId(), language);
        ChannelDTO channel = channelService.getById(playlist.getChannelId(), language);
        VideoEntity videoEntity = videoService.get(dto.getVideoId(), language);

        if (!channel.getProfileId().equals(profileId)) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);
        }

        PlaylistVideoEntity playlistVideo = getByPlaylistIdAndVideoId(playlist.getId(), videoEntity.getId(), language);

        playlistVideo.setOrderNumber(dto.getOrderNumber());

        playlistVideoRepository.save(playlistVideo);

        return "Updated";
    }

    public String delete(Integer profileId, PlaylistVideoDTO dto, AppLanguage language) {
        PlaylistEntity playlist = playlistService.get(dto.getPlaylistId(), language);
        ChannelDTO channel = channelService.getById(playlist.getChannelId(), language);
        VideoEntity videoEntity = videoService.get(dto.getVideoId(), language);

        if (!channel.getProfileId().equals(profileId)) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);
        }

        PlaylistVideoEntity playlistVideo = getByPlaylistIdAndVideoId(playlist.getId(), videoEntity.getId(), language);

        playlistVideoRepository.delete(playlistVideo);



        return "Deleted successfully";
    }

    public List<PlaylistVideoInfoDTO> getPlaylistVideoInfo(Integer playlistId, AppLanguage language) {
        List<PlaylistVideoInfoMapper> playlistVideoInfoMappers = playlistVideoRepository.getPlaylistVideoInfo(playlistId);
        List<PlaylistVideoInfoDTO> dtoList = new ArrayList<>();
        for (PlaylistVideoInfoMapper mapper : playlistVideoInfoMappers) {
            dtoList.add(mapToDTO(mapper,language));
        }

        return dtoList;
    }
    private PlaylistVideoInfoDTO mapToDTO(PlaylistVideoInfoMapper mapper,AppLanguage language) {
        PlaylistVideoInfoDTO dto = new PlaylistVideoInfoDTO();
        dto.setPlaylistId(mapper.getPlaylistId());

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(mapper.getVideoId());
        AttachDTO attachDTO=new AttachDTO();
        AttachEntity attachEntity = attachService.get(mapper.getVideoPreviewAttachId(), language);
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getUrl());
        videoDTO.setAttach(attachDTO);
        videoDTO.setTitle(mapper.getVideoTitle());
        videoDTO.setDuration(mapper.getVideoDuration());
        dto.setVideo(videoDTO);

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(mapper.getChannelId());
        channelDTO.setName(mapper.getChannelName());
        dto.setChannel(channelDTO);

        dto.setCreatedDate(mapper.getPlaylistVideoCreatedDate());
        dto.setOrderNumber(mapper.getPlaylistVideoOrderNumber());

        return dto;
    }


    public PlaylistVideoEntity getByPlaylistIdAndVideoId(Integer playlistId, String videoId, AppLanguage language) {
        return playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId).orElseThrow(() -> {
            log.warn("Playlist_video not found");
            return new AppBadException(resourceBundleService.getMessage("playlist_video.not.found", language));
        });
    }

}
