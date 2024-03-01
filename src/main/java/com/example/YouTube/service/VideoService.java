package com.example.YouTube.service;

import com.example.YouTube.config.CustomUserDetails;
import com.example.YouTube.dto.*;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.enums.VideoStatus;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.mapper.VideoFullInfoMapper;
import com.example.YouTube.mapper.VideoShortInfoMapper;
import com.example.YouTube.mapper.VideoShortInfoPaginationMapper;
import com.example.YouTube.repository.ChannelRepository;
import com.example.YouTube.repository.PlaylistVideoRepository;
import com.example.YouTube.repository.VideoRepository;
import com.example.YouTube.repository.VideoTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private VideoWatchedService videoWatchedService;

    @Autowired
    private VideoTagRepository videoTagRepository;

    @Autowired
    private PlaylistVideoService playlistVideoService;

    @Autowired
    private VideoTagService videoTagService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;


    /**
     * this method is used to create a video
     */

    public VideoCreateDTO create(VideoCreateDTO dto, CustomUserDetails currentUser, AppLanguage language) {

        checkProfileRole(currentUser, ProfileRole.ROLE_USER, language);
        checkProfileIsOwnerThisChannel(currentUser.getId(), dto.getChannelId(), language);
        List<VideoEntity> videoList = videoRepository.findByAttachId(dto.getAttachId());
//        List<PlaylistVideoEntity> playListVideo = playlistVideoRepository.findByVideoId(dto.getAttachId());

        if (videoList != null) {
            for (VideoEntity entity : videoList) {

                // to check if a video is in a single category
                if (entity.getAttachId().equals(dto.getAttachId()) && entity.getCategoryId().equals(dto.getCategoryId())) {
                    log.warn("One video must be in one category{}", dto.getCategoryId());
                    throw new AppBadException(resourceBundleService.getMessage("One.video.must.be.in.one.category", language));
                }

                //to check if a video is in a single playlist
//                List<Integer> playlistVideoList = new LinkedList<>();
//
//                for (PlaylistVideoEntity playlistVideoEntity : playListVideo) {
//                    playlistVideoList.add(playlistVideoEntity.getPlaylistId());
//                }
//                List<Integer> playlistVideo = dto.getPlaylistVideo();
//
//                if (containsAny(playlistVideoList, playlistVideo)) {
//                    log.warn("One video must be in one playList{}", dto.getAttachId());
//                    throw new AppBadException(resourceBundleService.getMessage("One.video.must.be.in.one.playList", language));
//                }

            }
        }

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

        videoTagService.create(entity.getId(), dto.getTagList());
        playlistVideoService.create(entity.getId(), dto.getPlaylistVideo());

        dto.setCreatedDate(LocalDateTime.now());

        return dto;
    }

    public static <T> boolean containsAny(List<T> list1, List<T> list2) {
        for (T item : list2) {
            if (list1.contains(item)) {
                return true;
            }
        }
        return false;
    }


    /**
     * this method is used to update video detail
     */
    public Boolean update(String id, CustomUserDetails currentUser, VideoCreateDTO dto, AppLanguage language) {

        Iterable<ChannelEntity> all = channelRepository.findAll();
        String channelId = "";
        for (ChannelEntity channelEntity : all) {
            if (channelEntity.getProfileId().equals(currentUser.getId())) {
                channelId = channelEntity.getId();
            }
        }

        checkProfileRole(currentUser, ProfileRole.ROLE_USER, language);
        checkProfileIsOwnerThisChannel(currentUser.getId(), channelId, language);
        VideoEntity video = get(id, language);

        // marge PlayList
        List<Integer> newPlaylist = dto.getPlaylistVideo();
        if (newPlaylist != null) {
            mergePlayList(id, newPlaylist);
        }

        // marge TagList
        List<String> newTagList = dto.getTagList();
        if (newTagList != null) {
            mergeTagName(id, newTagList);
        }

        if (dto.getTitle() != null) {
            video.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            video.setDescription(dto.getDescription());
        }
        if (dto.getPreviewAttachId() != null) {
            video.setPreviewAttachId(dto.getPreviewAttachId());
        }
        videoRepository.save(video);
        return true;
    }


    public void mergeTagName(String videoId, List<String> newVideoTagList) {

        List<VideoTagEntity> videoTagList = videoTagRepository.findByVideoIdList(videoId);

        Set<String> oldVideoTagListId = videoTagList.stream()
                .map(VideoTagEntity::getTagName)
                .collect(Collectors.toSet());

        List<String> toDelete = oldVideoTagListId.stream()
                .filter(id -> !newVideoTagList.contains(id))
                .collect(Collectors.toList());

        for (String tagsId : newVideoTagList) {
            if (!oldVideoTagListId.contains(tagsId)) {
                VideoTagEntity newVideoTag = new VideoTagEntity();
                newVideoTag.setVideoId(videoId);
                newVideoTag.setTagName(tagsId);
                videoTagRepository.save(newVideoTag);
            }
        }

        for (String tagName : toDelete) {
            videoTagRepository.deleteByVideoIdAndTagName(videoId, tagName);
        }
    }

    public void mergePlayList(String videoId, List<Integer> newPlayListIds) {
        // Eskilar ro'yhati
        List<PlaylistVideoEntity> oldPlayList = playlistVideoRepository.findByVideoId(videoId);


        Set<Integer> oldPlayListId = oldPlayList.stream()
                .map(PlaylistVideoEntity::getPlaylistId)
                .collect(Collectors.toSet());

        // Eskilar ro'yhatidan chiqarilgan yangi typelar

        List<Integer> toDelete = oldPlayListId.stream()
                .filter(id -> !newPlayListIds.contains(id))
                .collect(Collectors.toList());

        // Yangi typelarni qo'shish
        for (Integer typeId : newPlayListIds) {
            if (!oldPlayListId.contains(typeId)) {
                PlaylistVideoEntity newPlayListEntity = new PlaylistVideoEntity();
                newPlayListEntity.setVideoId(videoId);
                newPlayListEntity.setPlaylistId(typeId);
                playlistVideoRepository.save(newPlayListEntity);
            }
        }

        // Eskilar ro'yhatidan o'chirish
        for (Integer typeId : toDelete) {
            playlistVideoRepository.deleteByVideoIdAndPlaylistId(videoId, typeId);
        }
    }


    /**
     * this method is used to update video status
     */
    public Boolean updateStatus(String id, CustomUserDetails currentUser, VideoStatusDTO dto, AppLanguage language) {

        checkProfileRole(currentUser, ProfileRole.ROLE_USER, language);
        checkProfileIsOwnerThisChannel(currentUser.getId(), dto.getChannelId(), language);

        VideoEntity entity = get(id, language);
        entity.setVideoStatus(dto.getVideoStatus());
        videoRepository.save(entity);
        return true;
    }


    /**
     * this method is used to increase video view count by videoId
     */
    public Boolean increaseViewCount(String videoId, Integer profileId, AppLanguage language) {
        get(videoId, language);
        return videoWatchedService.save(videoId, profileId);
    }


    /**
     * this method is used to search video by title
     */
    public List<VideoListShortInfoDTO> search(String title, AppLanguage language) {
        Iterable<VideoEntity> all = videoRepository.findAll();
        List<VideoListShortInfoDTO> dtoList = new LinkedList<>();
        for (VideoEntity entity : all) {
            if (entity.getTitle().toLowerCase().contains(title.toLowerCase()) && entity.getVideoStatus().equals(VideoStatus.PUBLIC)) {
                Optional<VideoShortInfoMapper> optional = videoRepository.getVideoShortInfo(entity.getId());

                if (optional.isPresent()) {
                    VideoShortInfoMapper mapper = optional.get();
                    VideoListShortInfoDTO video = new VideoListShortInfoDTO();
                    video.setId(mapper.getVideoId());
                    video.setTitle(mapper.getTitle());
                    video.setViewCount(mapper.getViewCount());
                    video.setPublishedDate(mapper.getPublishedDate());
                    video.setDuration(mapper.getDuration());
                    if (mapper.getPreviewAttachId() != null) {
                        video.setPreviewAttach(attachService.getURL(mapper.getPreviewAttachId(), language));
                    }
                    ChannelDTO channelDTO = new ChannelDTO();
                    channelDTO.setId(mapper.getChannelId());
                    channelDTO.setName(mapper.getChannelName());
                    channelDTO.setPhotoId(mapper.getPhotoId());
                    video.setChannel(channelDTO);

                    dtoList.add(video);
                }
            }
        }
        return dtoList;
    }


    /**
     * this method is used to pagination video by categoryId
     */
    public PageImpl<VideoListShortInfoDTO> paginationCategoryId(Integer page, Integer size, Integer categoryId, AppLanguage language) {

        Pageable paging = PageRequest.of(page - 1, size);

        Page<VideoShortInfoMapper> videoPage = videoRepository.getVideoShortInfoPagination(paging, categoryId);

        List<VideoShortInfoMapper> entityList = videoPage.getContent();
        long totalElement = videoPage.getTotalElements();

        List<VideoListShortInfoDTO> dtoList = new LinkedList<>();
        for (VideoShortInfoMapper entity : entityList) {
            VideoListShortInfoDTO videoDTO = new VideoListShortInfoDTO();
            videoDTO.setId(entity.getId());
            videoDTO.setTitle(entity.getTitle());
            if (entity.getPreviewAttachId() != null) {
                videoDTO.setPreviewAttach(attachService.getURL(entity.getPreviewAttachId(), language));
            }
            videoDTO.setPublishedDate(entity.getPublishedDate());
            videoDTO.setViewCount(entity.getViewCount());
            videoDTO.setDuration(entity.getDuration());

            ChannelDTO channelDTO = new ChannelDTO();
            channelDTO.setId(entity.getChannelId());
            channelDTO.setName(entity.getChannelName());
            channelDTO.setPhotoId(entity.getPhotoId());
            videoDTO.setChannel(channelDTO);

            dtoList.add(videoDTO);
        }
        return new PageImpl<>(dtoList, paging, totalElement);
    }


    /**
     * this method is used to get byId videos
     */
    public VideoFullInfoDTO getById(String id, CustomUserDetails currentUser, AppLanguage language) {

        VideoEntity entity = get(id, language);
        Integer profileId = currentUser.getId();
        if (entity.getVideoStatus().equals(VideoStatus.PRIVATE)) {
            ProfileEntity profileEntity = profileService.get(profileId, language);
            ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
            if (!profileEntity.getRole().equals(ProfileRole.ROLE_ADMIN) || !channelEntity.getProfileId().equals(profileId)) {
                log.warn("This video is private{}", id);
                throw new AppBadException(resourceBundleService.getMessage("This.video.is.private", language));
            }
        }

        Optional<VideoFullInfoMapper> videFullInfo = videoRepository.getVideFullInfo(id);
        if (videFullInfo.isEmpty()) {
            log.warn("Video not found{}", id);
            throw new AppBadException(resourceBundleService.getMessage("Video.not.found", language));
        }

        VideoFullInfoMapper mapper = videFullInfo.get();
        VideoFullInfoDTO videoDTO = new VideoFullInfoDTO();
        videoDTO.setId(mapper.getVideoId());
        videoDTO.setTitle(mapper.getTitle());
        videoDTO.setDescription(mapper.getDescription());
        videoDTO.setViewCount(mapper.getViewCount());
        videoDTO.setSharedCount(mapper.getSharedCount());
        videoDTO.setLikeCount(mapper.getLikeCount());
        videoDTO.setDislikeCount(mapper.getDislikeCount());
        videoDTO.setSharedCount(mapper.getSharedCount());
        videoDTO.setDuration(mapper.getDuration());
        if (mapper.getEmotion() != null) {
            videoDTO.setEmotion(mapper.getEmotion());
        }


        if (mapper.getPreviewAttachId() != null) {
            videoDTO.setPreviewAttach(attachService.getURL(mapper.getPreviewAttachId(), language));
        }
        videoDTO.setPublishedDate(mapper.getPublishedDate());

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(mapper.getChannelId());
        channelDTO.setName(mapper.getChannelName());
        channelDTO.setPhotoId(mapper.getPhotoId());
        videoDTO.setChannel(channelDTO);

        CategoryDTO category = new CategoryDTO();
        category.setId(mapper.getCategoryId());
        category.setName(mapper.getCategoryName());
        videoDTO.setCategory(category);

        AttachDTO attach = new AttachDTO();
        attach.setId(mapper.getAttachId());
        attach.setUrl(mapper.getUrl());
        attach.setDuration(mapper.getDuration());
        videoDTO.setAttach(attach);
        videoDTO.setTagListJson(mapper.getTagListJson());

        return videoDTO;
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


    /**
     * this method is used to get video by tagId and pages
     */
    public PageImpl<VideoListShortInfoDTO> getByTagId(Integer page, Integer size, AppLanguage language, Integer tagId) {

        Pageable paging = PageRequest.of(page - 1, size);

        Page<VideoShortInfoMapper> videoPage = videoRepository.getVideoShortInfoInteger(paging, tagId);

        List<VideoShortInfoMapper> entityList = videoPage.getContent();
        long totalElement = videoPage.getTotalElements();

        List<VideoListShortInfoDTO> dtoList = new LinkedList<>();
        for (VideoShortInfoMapper entity : entityList) {
            VideoListShortInfoDTO videoDTO = new VideoListShortInfoDTO();
            videoDTO.setId(entity.getVideoId());
            videoDTO.setTitle(entity.getTitle());
            videoDTO.setPublishedDate(entity.getPublishedDate());
            videoDTO.setViewCount(entity.getViewCount());
            videoDTO.setDuration(entity.getDuration());
            if (entity.getPreviewAttachId() != null) {
                videoDTO.setPreviewAttach(attachService.getURL(entity.getPreviewAttachId(), language));
            }

            ChannelDTO channelDTO = new ChannelDTO();
            channelDTO.setId(entity.getChannelId());
            channelDTO.setName(entity.getChannelName());
            channelDTO.setPhotoId(entity.getPhotoId());
            videoDTO.setChannel(channelDTO);

            dtoList.add(videoDTO);
        }
        return new PageImpl<>(dtoList, paging, totalElement);
    }

    public PageImpl<VideoListPaginationDTO> getVideoListForAdmin(Integer page, Integer size, CustomUserDetails currentUser, AppLanguage language) {
        checkProfileRole(currentUser, ProfileRole.ROLE_ADMIN, language);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<VideoShortInfoPaginationMapper> entityPage = videoRepository.getVideoListForAdmin(pageable);

        List<VideoShortInfoPaginationMapper> entityList = entityPage.getContent();
        long totalElements = entityPage.getTotalElements();

        List<VideoListPaginationDTO> dtoList = new LinkedList<>();
        for (VideoShortInfoPaginationMapper entity : entityList) {
            VideoListPaginationDTO videoDTO = new VideoListPaginationDTO();
            videoDTO.setId(entity.getId());
            videoDTO.setTitle(entity.getTitle());
            if (entity.getPreviewAttachId() != null) {
                videoDTO.setPreviewAttach(attachService.getURL(entity.getPreviewAttachId(), language));
            }
            videoDTO.setPublishedDate(entity.getPublishedDate());

            ChannelDTO channelDTO = new ChannelDTO();
            channelDTO.setId(entity.getChannelId());
            channelDTO.setName(entity.getChannelName());
            channelDTO.setPhotoId(entity.getPhotoId());
            videoDTO.setChannel(channelDTO);

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(entity.getProfileId());
            profileDTO.setName(entity.getProfileName());
            profileDTO.setSurname(entity.getProfileSurname());
            videoDTO.setOwner(profileDTO);

            videoDTO.setPlayListJson(entity.getPlayListJson());
            dtoList.add(videoDTO);
        }
        return new PageImpl<>(dtoList, pageable, totalElements);
    }

    private void checkProfileRole(CustomUserDetails currentUser, ProfileRole role, AppLanguage language) {
        if (!currentUser.getRole().equals(role)) {
            log.warn("You have not access{}", currentUser.getId());
            throw new AppBadException(resourceBundleService.getMessage("You.have.not.access", language));
        }
    }


    private void checkProfileIsOwnerThisChannel(Integer profileId, String channelId, AppLanguage language) {
        ChannelEntity channelEntity = channelService.get(channelId, language);
        if (!profileId.equals(channelEntity.getProfileId())) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("Profile.not.found", language) + " -->> " + profileId);
        }
    }


}
