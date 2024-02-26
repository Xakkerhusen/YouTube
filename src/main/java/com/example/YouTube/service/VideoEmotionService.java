package com.example.YouTube.service;

import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.VideoDTO;
import com.example.YouTube.dto.VideoEmotionDTO;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.LikeStatus;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.VideoEmotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoEmotionService {

    @Autowired
    private VideoEmotionRepository videoEmotionRepository;

    @Autowired
    private ResourceBundleService resourceBundleService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private ProfileService profileService;


    public VideoEmotionDTO createLike(VideoEmotionDTO dto, AppLanguage language) {
        return changeEmotionStatus(dto, LikeStatus.DISLIKE, LikeStatus.LIKE, language);
    }


    public VideoEmotionDTO createDislike(VideoEmotionDTO dto, AppLanguage language) {
        return changeEmotionStatus(dto, LikeStatus.LIKE, LikeStatus.DISLIKE, language);
    }


    public Boolean remove(Integer pId, String vId, AppLanguage language) {
        VideoEmotionEntity articleLikeEntity = get(pId, vId, language);
        videoEmotionRepository.delete(articleLikeEntity);
        return true;
    }


    @NotNull
    private VideoEmotionDTO changeEmotionStatus(VideoEmotionDTO dto, LikeStatus oldEmotion, LikeStatus newEmotion, AppLanguage language) {

        VideoEntity videoEntity = videoService.get(dto.getVideoId(), language);
        ProfileEntity profileEntity = profileService.get(dto.getProfileId(), language);
        LikeStatus newStatus = dto.getStatus();

        Optional<VideoEmotionEntity> optional = videoEmotionRepository.findByVideoIdAndProfileId(dto.getVideoId(), dto.getProfileId());

        if (optional.isPresent()) {
            VideoEmotionEntity video = optional.get();
            LikeStatus oldStatus = video.getStatus();

            if (oldStatus.equals(oldEmotion) && newStatus.equals(newEmotion)) {
                videoEmotionRepository.updateStatus(video.getId(), newStatus);
            }
            dto.setId(video.getId());
            dto.setCreatedDate(LocalDateTime.now());
        }

        if (optional.isEmpty()) {

            VideoEmotionEntity video = new VideoEmotionEntity();
            video.setVideoId(videoEntity.getId());
            video.setProfileId(profileEntity.getId());
            video.setStatus(newStatus);
            videoEmotionRepository.save(video);

            dto.setId(video.getId());
            dto.setCreatedDate(LocalDateTime.now());
        }
        return dto;
    }


    @NotNull
    private VideoEmotionEntity get(Integer pId, String vId, AppLanguage language) {
        Optional<VideoEmotionEntity> optional = videoEmotionRepository.findByVideoIdAndProfileId(vId, pId);
        if (optional.isEmpty()) {
            log.warn("Video not found{}", pId);
            throw new AppBadException(resourceBundleService.getMessage("Video.like.not.found", language));
        }
        return optional.get();
    }


    public List<VideoEmotionDTO> getUserLikedList(Integer profileId, AppLanguage language) {
        profileService.get(profileId, language);

        List<VideoEmotionEntity> profileLikedList = videoEmotionRepository.findByProfileIdOrderByCreatedDate(profileId);
        List<VideoEmotionDTO> dtoList = new LinkedList<>();

        for (VideoEmotionEntity videoEmotion : profileLikedList) {
            VideoEmotionDTO emotion = new VideoEmotionDTO();

            VideoEntity videoEntity = videoService.get(videoEmotion.getVideoId(), language);
            ChannelEntity channelEntity = channelService.get(videoEntity.getChannelId(), language);

            VideoDTO videoDTO = new VideoDTO();
            ChannelDTO channelDTO = new ChannelDTO();
            AttachDTO attachDTO = new AttachDTO();

            videoDTO.setId(videoEntity.getId());
            videoDTO.setDuration(videoEntity.getDuration());
            videoDTO.setTitle(videoEntity.getTitle());

            //  channel
            channelDTO.setId(channelEntity.getId());
            channelDTO.setName(channelEntity.getName());
            videoDTO.setChannel(channelDTO);

            //  attach
//            attachDTO.setUrl(videoEntity.getPreviewAttach().getUrl());
            attachDTO.setId(videoEntity.getPreviewAttachId());
            videoDTO.setPreviewAttach(attachDTO);
            emotion.setVideo(videoDTO);
            dtoList.add(emotion);
        }
        return dtoList;
    }


    public List<VideoEmotionDTO> getUserLikedListByAmin(Integer adminId, Integer profileId, AppLanguage language) {
        ProfileEntity profileEntity = profileService.get(adminId, language);
        if (!profileEntity.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            log.warn("You have not access{}", adminId);
            throw new AppBadException(resourceBundleService.getMessage("You.have.not.access", language));
        }
        return getUserLikedList(profileId, language);
    }


}
