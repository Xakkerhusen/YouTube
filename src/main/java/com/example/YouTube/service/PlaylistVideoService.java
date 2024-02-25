package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.PlaylistRepository;
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
    private final PlaylistRepository playlistRepository;
    private final AttachService attachService;
    public PlaylistVideoService(PlaylistVideoRepository playlistVideoRepository, PlaylistService playlistService,
                                VideoService videoService, ChannelService channelService,
                                ResourceBundleService resourceBundleService, PlaylistRepository playlistRepository,
                                AttachService attachService) {
        this.playlistVideoRepository = playlistVideoRepository;
        this.playlistService = playlistService;
        this.videoService = videoService;
        this.channelService = channelService;
        this.resourceBundleService = resourceBundleService;
        this.playlistRepository = playlistRepository;
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
//        playlist.setVideoCount(playlist.getVideoCount() + 1);
//        playlistRepository.save(playlist);

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

//        playlist.setVideoCount(playlist.getVideoCount() - 1);
//        playlistRepository.save(playlist);

        return "Deleted successfully";
    }


    public List<PlaylistVideoInfoDTO> getPlaylistVideoInfo(Integer profileId, Integer playlistId, AppLanguage language) {

        List<PlaylistVideoEntity> allByPlaylistId = playlistVideoRepository.findAllByPlaylistId(playlistId);

        PlaylistEntity playlist = playlistService.get(playlistId, language);
        ChannelEntity channelEntity = channelService.get(playlist.getChannelId(), language);
        if (!channelEntity.getProfileId().equals(profileId)) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);
        }

        List<PlaylistVideoInfoDTO> dtoList = new ArrayList<>();

        for (PlaylistVideoEntity entity : allByPlaylistId) {
            PlaylistVideoInfoDTO playlistVideoInfoDTO = new PlaylistVideoInfoDTO();
            VideoDTO videoDTO = new VideoDTO();
            VideoEntity videoEntity = videoService.get(entity.getVideoId(), language);
            videoDTO.setId(videoEntity.getId());
            AttachDTO attachDTO = new AttachDTO();
            AttachEntity attachEntity = attachService.get(videoEntity.getAttachId(), language);
            attachDTO.setId(attachEntity.getId());
            attachDTO.setUrl(attachEntity.getUrl());
            videoDTO.setPreviewAttach(attachDTO);
            videoDTO.setTitle(videoEntity.getTitle());
            videoDTO.setDuration(videoEntity.getDuration());

            playlistVideoInfoDTO.setPlaylistId(entity.getPlaylistId());
            playlistVideoInfoDTO.setVideo(videoDTO);

            ChannelDTO channelDTO = new ChannelDTO();
            channelDTO.setId(channelEntity.getId());
            channelDTO.setName(channelEntity.getName());
            playlistVideoInfoDTO.setChannel(channelDTO);

            playlistVideoInfoDTO.setCreatedDate(entity.getCreatedDate());
            playlistVideoInfoDTO.setOrderNumber(entity.getOrderNumber());

            dtoList.add(playlistVideoInfoDTO);


        }
        return dtoList;
    }


    public PlaylistVideoEntity getByPlaylistIdAndVideoId(Integer playlistId, String videoId, AppLanguage language) {
        return playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId).orElseThrow(() -> {
            log.warn("Playlist_video not found");
            return new AppBadException(resourceBundleService.getMessage("playlist_video.not.found", language));
        });
    }

}
