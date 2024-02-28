package com.example.YouTube.service;


import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.CreatedLikeDTO;
import com.example.YouTube.dto.VideoDTO;
import com.example.YouTube.dto.VideoLikeInfoDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.VideoLikeEntity;
import com.example.YouTube.entity.VideoLikeHistoryEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.mapper.VideolikeInfoMapper;
import com.example.YouTube.repository.VideoLikeHistoryRepository;
import com.example.YouTube.repository.VideoLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoLikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private VideoLikeHistoryRepository videoLikeHistoryRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private AttachService attachService;

    public Object create(String videoId, Integer profileId, CreatedLikeDTO dto, AppLanguage language) {
        videoService.get(videoId, language);
        Optional<VideoLikeEntity> optional = videoLikeRepository.findTop1ByVideoId(videoId, profileId);

        VideoLikeEntity videLikeEntity = new VideoLikeEntity();
        VideoLikeHistoryEntity entity2 = new VideoLikeHistoryEntity();
        if (optional.isEmpty()) {
            return getObject(videoId, profileId, dto, videLikeEntity, entity2);
        }

        VideoLikeEntity entity = optional.get();
        if (entity.getType().equals(dto.getStatus().toString())) {
            videoLikeRepository.deleteById(entity.getId());
            videoLikeHistoryRepository.deleteVideoLike(entity.getId());
            return "SUCCESS delete";
        } else if (entity.getVideoId().equals(videoId) && !entity.getProfileId().equals(profileId)) {
            return getObject(videoId, profileId, dto, videLikeEntity, entity2);
        } else {
            videoLikeHistoryRepository.updateVideoLike(entity.getId(), String.valueOf(dto.getStatus()));
            return videoLikeRepository.update(entity.getId(), String.valueOf(dto.getStatus())) != 0 ? "update" : "articleId not found";
        }

    }
/**this method is used to get a list of videos that the user has liked 👇🏻*/
    public List<VideoLikeInfoDTO> userLikedVideoList(Integer profileId, AppLanguage language) {
        return getVideoLikeInfoDTOS(profileId, language);
    }
    /**this method is used to retrieve the list of videos liked by the user with the given user id 👇🏻*/
    public List<VideoLikeInfoDTO> userLikedVideoListByUserId(Integer id, AppLanguage language) {
        return getVideoLikeInfoDTOS(id, language);
    }

    @NotNull
    private List<VideoLikeInfoDTO> getVideoLikeInfoDTOS(Integer id, AppLanguage language) {
        List<VideolikeInfoMapper> entities = videoLikeHistoryRepository.getByProfileId(id);
        if (entities.isEmpty()) {
            log.warn(resourceBundleService.getMessage("this.user.has.not.liked.or.disliked.any.videos", language));
            throw new ArithmeticException(resourceBundleService.getMessage("this.user.has.not.liked.or.disliked.any.videos", language));
        }
        List<VideoLikeInfoDTO> dtoList = new LinkedList<>();
        for (VideolikeInfoMapper entity : entities) {
            dtoList.add(toDo(entity,language));
        }
        return dtoList;
    }


    @NotNull
    private Object getObject(String videoId, Integer profileId, CreatedLikeDTO dto, VideoLikeEntity videLikeEntity, VideoLikeHistoryEntity entity2) {
        videLikeEntity.setVideoId(videoId);
        videLikeEntity.setProfileId(profileId);
        videLikeEntity.setType(String.valueOf(dto.getStatus()));
        videoLikeRepository.save(videLikeEntity);
        entity2.setVideoId(videoId);
        entity2.setProfileId(profileId);
        entity2.setType(String.valueOf(dto.getStatus()));

        videoLikeHistoryRepository.save(entity2);
        return "SUCCESS save";
    }

    private VideoLikeInfoDTO toDo(VideolikeInfoMapper entity, AppLanguage language) {
        AttachEntity attachEntity = attachService.get(entity.getPreviewAttachId(), language);
        VideoLikeInfoDTO dto = new VideoLikeInfoDTO();
        dto.setVideoLikeId(entity.getVideoLikeId());

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(entity.getVideoId());
        videoDTO.setTitle(entity.getVideoName());
        dto.setVideo(videoDTO);
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(entity.getChannalId());
        channelDTO.setName(entity.getChannalName());
        dto.setChannel(channelDTO);
        dto.setDuration(entity.getDuration());
        dto.setPreviewAttach(attachService.toDTO(attachEntity));
        return dto;
    }
}
