package com.example.YouTube.service;

import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.TagNameDTO;
import com.example.YouTube.dto.VideoTagCreateDTO;
import com.example.YouTube.dto.VideoTagDTO;
import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.entity.TagNameEntity;
import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.entity.VideoTagEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.VideoRepository;
import com.example.YouTube.repository.VideoTagRepository;
import com.example.YouTube.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoTagService {
    @Autowired
    private TagNameService tagNameService;
    @Autowired
    private VideoTagRepository videoTagRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ResourceBundleService resourceBundleService;

    /**
     * This method used to create tag to video
     */
    public void add(VideoTagCreateDTO tagCreateDTO, AppLanguage language) {
        /*For checking*/
        tagNameService.get(tagCreateDTO.getTagId(), language);
        Optional<VideoEntity> optional = videoRepository.findById(tagCreateDTO.getVideoId());
        if (optional.isEmpty()) {
            throw new AppBadException(resourceBundleService.getMessage("video.not.found", language));
        }
        ChannelDTO channelDTO = channelService.getById(optional.get().getChannelId(), language);
        if (!channelDTO.getProfileId().equals(SpringSecurityUtil.getCurrentUser().getId())) {
            throw new AppBadException(resourceBundleService.getMessage("you.cannot.add.tag.to.this.video", language));
        }

        VideoTagEntity videoTag = new VideoTagEntity();
        videoTag.setTagId(tagCreateDTO.getTagId());
        videoTag.setVideoId(tagCreateDTO.getVideoId());
        videoTagRepository.save(videoTag);
    }

    /**
     * This method used to delete tag from video
     */
    public void delete(VideoTagCreateDTO tagCreateDTO, AppLanguage language) {
        tagNameService.get(tagCreateDTO.getTagId(), language);
        Optional<VideoEntity> optional = videoRepository.findById(tagCreateDTO.getVideoId());
        if (optional.isEmpty()) {
            throw new AppBadException(resourceBundleService.getMessage("video.not.found", language));
        }
        ChannelDTO channelDTO = channelService.getById(optional.get().getChannelId(), language);
        if (!channelDTO.getProfileId().equals(SpringSecurityUtil.getCurrentUser().getId())) {
            throw new AppBadException(resourceBundleService.getMessage("you.cannot.add.tag.to.this.video", language));
        }

        Integer result = videoTagRepository.deleteByVideoIdAndTagId(tagCreateDTO.getVideoId(), tagCreateDTO.getTagId());
        if (result < 1)
            throw new AppBadException(resourceBundleService.getMessage("no.such.tag.found.in.this.video", language));
    }

    /**
     * This method used to get all video tag by video id
     */
    public List<VideoTagDTO> getVideoTagListByVideoId(String videoId, AppLanguage language) {
        List<VideoTagDTO> dtoList = new LinkedList<>();
        for (VideoTagEntity entity : videoTagRepository.findByVideoId(videoId)) {
            dtoList.add(toDTO(entity, language));
        }
        return dtoList;
    }


    /**
     * This method is used to load object data from Entity class to DTO class
     */
    private VideoTagDTO toDTO(VideoTagEntity entity, AppLanguage language) {
        TagNameEntity tagNameEntity = tagNameService.get(entity.getTagId(), language);
        TagNameDTO tagNameDTO = new TagNameDTO();
        tagNameDTO.setId(tagNameEntity.getId());
        tagNameDTO.setTagName(tagNameEntity.getTagName());

        VideoTagDTO dto = new VideoTagDTO();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setTag(tagNameDTO);
        return dto;
    }


    public void create(String videoId, List<String> tagList) {
        for (String play : tagList) {
            create(videoId, play);
        }
    }

    public void create(String videoId, String tagListId) {
        VideoTagEntity entity = new VideoTagEntity();
        entity.setVideoId(videoId);
        entity.setTagName(tagListId);
        videoTagRepository.save(entity);
    }


}
