package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.enums.VideoStatus;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.AttachRepository;
import com.example.YouTube.repository.VideoRepository;
import com.example.YouTube.utils.SpringSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ResourceBundleService resourceBundleService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CategoryService categoryService;


    /**
     * this method is used to create a video
     */
    public VideoCreateDTO create(VideoCreateDTO dto, AppLanguage language) {
        VideoEntity entity = new VideoEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setAttachId(dto.getAttachId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setChannelId(dto.getChannelId());
        entity.setPreviewAttachId(dto.getPreviewAttachId());
        entity.setVideoStatus(dto.getVideoStatus());
        entity.setVideoType(dto.getVideoType());
        AttachEntity attachEntity = attachService.get(dto.getAttachId(), language);
        entity.setDuration(attachEntity.getDuration());
        entity.setPublishedDate(LocalDateTime.now());

        videoRepository.save(entity);
        dto.setCreatedDate(LocalDateTime.now());

        return dto;
    }


    /**
     * this method is used to update video detail
     */
    public Boolean update(String id, VideoCreateDTO dto, AppLanguage language) {
        VideoEntity entity = get(id, language);
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPreviewAttachId() != null) {
            entity.setPreviewAttachId(dto.getPreviewAttachId());
        }
        videoRepository.save(entity);
        return true;
    }


    /**
     * this method is used to update video status
     */
    public Boolean updateStatus(String id, VideoStatusDTO dto, AppLanguage language) {
        VideoEntity entity = get(id, language);
        entity.setVideoStatus(dto.getVideoStatus());
        videoRepository.save(entity);
        return true;
    }


    /**
     * this method is used to increase video view count by videoId
     */
    public Boolean increaseViewCount(String videoId, AppLanguage language) {
        get(videoId, language);
        return videoRepository.increaseViewCount(videoId) != 0;
    }


    /**
     * this method is used to search video by title
     */
    public List<VideoDTO> search(String title, AppLanguage language) {
        Iterable<VideoEntity> all = videoRepository.findAll();
        List<VideoDTO> dtoList = new LinkedList<>();
        for (VideoEntity entity : all) {
            if (entity.getTitle().toLowerCase().contains(title.toLowerCase())) {
                dtoList.add(toDTOShortInfo(entity, language));
            }
        }
        return dtoList;
    }


    /**
     * this method is used to pagination video by categoryId
     */
    public PageImpl<VideoDTO> paginationCategoryId(Integer page, Integer size, Integer categoryId, AppLanguage language) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable paging = PageRequest.of(page - 1, size, sort);

        Page<VideoEntity> videoPage = videoRepository.findAllByCategoryId(paging, categoryId);
        List<VideoEntity> entityList = videoPage.getContent();
        long totalElement = videoPage.getTotalElements();

        List<VideoDTO> dtoList = new LinkedList<>();
        for (VideoEntity entity : entityList) {
            dtoList.add(toDTOShortInfo(entity, language));
        }
        return new PageImpl<>(dtoList, paging, totalElement);
    }


    /**
     * this method is used to get byId videos
     */
    public VideoDTO getById(String id, AppLanguage language) {
        VideoEntity entity = get(id, language);
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        if (entity.getVideoStatus().equals(VideoStatus.PRIVATE)) {
            ProfileEntity profileEntity = profileService.get(profileId, language);
            ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
            if (!profileEntity.getRole().equals(ProfileRole.ROLE_ADMIN) || !channelEntity.getProfileId().equals(profileId)) {
                log.warn("This video is private{}", id);
                throw new AppBadException(resourceBundleService.getMessage("This.video.is.private", language));
            }
        }
        return toDTOFullInfo(entity, language);
    }


    /**
     * this method is used to pagination video list
     */
    public PageImpl<VideoDTO> paginationVideoList(Integer page, Integer size, AppLanguage language) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable paging = PageRequest.of(page - 1, size, sort);

        Page<VideoEntity> videoPage = videoRepository.findAll(paging);
        List<VideoEntity> entityList = videoPage.getContent();
        long totalElement = videoPage.getTotalElements();

        List<VideoDTO> dtoList = new LinkedList<>();
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        for (VideoEntity entity : entityList) {
            VideoDTO dtoShortInfo = toDTOShortInfo(entity, language);

            //  Profile detail  (id,name,surname)
            ProfileDTO profile = new ProfileDTO();
            ProfileEntity profileEntity = profileService.get(profileId, language);
            profile.setId(profileEntity.getId());
            profile.setName(profileEntity.getName());
            profile.setSurname(profileEntity.getSurname());
            dtoShortInfo.setProfile(profile);


            dtoList.add(dtoShortInfo);
        }
        return new PageImpl<>(dtoList, paging, totalElement);
    }


    /**
     * this method is used to pagination channel video list
     */
    public PageImpl<VideoDTO> paginationChannelVideoList(Integer page, Integer size, AppLanguage language, String id) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");

        Pageable paging = PageRequest.of(page - 1, size, sort);

        Page<VideoEntity> videoPage = videoRepository.findByChannelId(paging, id);
        List<VideoEntity> entityList = videoPage.getContent();
        long totalElement = videoPage.getTotalElements();

        List<VideoDTO> dtoList = new LinkedList<>();
        for (VideoEntity entity : entityList) {
            dtoList.add(toDTOPlaylistInfo(entity, language));
        }
        return new PageImpl<>(dtoList, paging, totalElement);

    }


    /**
     * this method is used to set video details
     * which sets the incoming entity to dto.
     */
    private VideoDTO toDTOPlaylistInfo(VideoEntity entity, AppLanguage language) {
        VideoDTO dto = new VideoDTO();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());

        // preview_attach
        AttachEntity attachEntity = attachService.get(entity.getAttachId(), language);
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getPath());
        dto.setPreviewAttach(attachDTO);

        // publishedDate, view count,duration
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setDuration(entity.getDuration());

        return dto;
    }


    /**
     * this method is used to set video details
     * which sets the incoming entity to dto.
     * short info
     */
    private VideoDTO toDTOShortInfo(VideoEntity entity, AppLanguage language) {
        VideoDTO dto = new VideoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());

        // preview_attach
        AttachEntity attachEntity = attachService.get(entity.getAttachId(), language);
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getPath());
        dto.setPreviewAttach(attachDTO);

        // channel
        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
        ChannelDTO channel = new ChannelDTO();
        AttachDTO attachDto = new AttachDTO();
        attachDto.setPath(channelEntity.getPhotoId());

        channel.setId(channel.getId());
        channel.setName(channelEntity.getName());
        channel.setPhoto(attachDto);
        dto.setChannel(channel);

        // publishedDate, view count,duration
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setDuration(entity.getDuration());

        return dto;
    }


//    tagList(id,name)
//    Like(isUserLiked,IsUserDisliked)


    /**
     * this method is used to set video details
     * which sets the incoming entity to dto.
     * full info
     */
    private VideoDTO toDTOFullInfo(VideoEntity entity, AppLanguage language) {
        VideoDTO dto = new VideoDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        dto.setDuration(entity.getDuration());

        //  Preview_attach detail (id,url)
        AttachEntity previewAttachEntity = attachService.get(entity.getPreviewAttachId(), language);
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(previewAttachEntity.getId());
        attachDTO.setUrl(previewAttachEntity.getPath());
        dto.setPreviewAttach(attachDTO);

        //  Attach detail (id,url,duration)
        AttachEntity attachEntity = attachService.get(entity.getAttachId(), language);
        AttachDTO attach = new AttachDTO();
        attach.setId(attachEntity.getId());
        attach.setUrl(attachEntity.getPath());
        attach.setDuration(attachEntity.getDuration());
        dto.setAttach(attach);

        //  Category detail (id,name)
        CategoryEntity categoryEntity = categoryService.get(entity.getCategoryId(), language);
        CategoryDTO category = new CategoryDTO();
        category.setId(categoryEntity.getId());
        category.setName(categoryEntity.getName());
        dto.setCategory(category);

        //  Channel detail (id,name,photo(url))
        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
        ChannelDTO channel = new ChannelDTO();
        AttachDTO attachDto = new AttachDTO();
        attachDto.setPath(channelEntity.getPhotoId());

        channel.setId(channel.getId());
        channel.setName(channelEntity.getName());
        channel.setPhoto(attachDto);
        dto.setChannel(channel);

        return dto;
    }


    /**
     * this method is used to get full video detail by videoId
     * and return one video details
     */
    public VideoEntity get(String id, AppLanguage language) {
        Optional<VideoEntity> optional = videoRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Video not found{}", id);
            throw new AppBadException(resourceBundleService.getMessage("Video.not.found", language));
        }
        return optional.get();
    }


}
