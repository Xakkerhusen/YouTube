package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.exp.AppBadException;
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
import java.util.Optional;

@Slf4j
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final ChannelService channelService;
    private final ResourceBundleService resourceBundleService;
    private final ProfileService profileService;
    private final AttachService attachService;
    private final PlaylistVideoRepository playlistVideoRepository;
    private final VideoService videoService;

    /**
     * Constructs a new PlaylistService with the specified dependencies.
     *
     * @param playlistRepository      The repository for playlist entities.
     * @param channelService          The service for channel entities.
     * @param resourceBundleService   The service for handling resource bundles.
     * @param profileService          The service for profile entities.
     * @param attachService           The service for attach entities.
     * @param playlistVideoRepository The repository for playlist video entities.
     * @param videoService            The service for video entities.
     */
    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, ChannelService channelService,
                           ResourceBundleService resourceBundleService, ProfileService profileService,
                           AttachService attachService, PlaylistVideoRepository playlistVideoRepository,
                           VideoService videoService) {
        this.playlistRepository = playlistRepository;
        this.channelService = channelService;
        this.resourceBundleService = resourceBundleService;
        this.profileService = profileService;
        this.attachService = attachService;
        this.playlistVideoRepository = playlistVideoRepository;
        this.videoService = videoService;
    }

    /**
     * Creates a new playlist.
     *
     * @param profileId The ID of the profile creating the playlist.
     * @param dto       DTO containing playlist information.
     * @param language  Language for error messages.
     * @return Success message on playlist creation.
     * @throws AppBadException If profile lacks permission to create the playlist.
     */
    public String create(Integer profileId, CreatePlaylistDTO dto, AppLanguage language) {
        ChannelDTO channelDTO = channelService.getById(dto.getChannelId(), language);

        if (!profileId.equals(channelDTO.getProfileId())) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);

        }
        PlaylistEntity entity = new PlaylistEntity();

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
     * Updates an existing playlist.
     *
     * @param playlistId The ID of the playlist to update.
     * @param profileId  The ID of the profile updating the playlist.
     * @param dto        DTO containing updated playlist information.
     * @param language   Language for error messages.
     * @return Success message on playlist update.
     * @throws AppBadException If profile lacks permission to update the playlist.
     */
    public String update(Integer playlistId, Integer profileId, PlaylistDTO dto, AppLanguage language) {
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);

        if (!profileId.equals(channelDTO.getProfileId())) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);

        }

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());

        playlistRepository.save(entity);
        return "Playlist has updated successfully";

    }

    /**
     * This method is used to update status of the playlist
     */
    public String updateStatus(Integer playlistId, Integer profileId, PlaylistDTO dto, AppLanguage language) {
        PlaylistEntity entity = get(playlistId, language);

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);

        if (!profileId.equals(channelDTO.getProfileId())) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);

        }
        entity.setStatus(dto.getStatus());
        entity.setUpdatedDate(LocalDateTime.now());

        playlistRepository.save(entity);
        return "Playlist status has updated successfully";

    }

    /**
     * Deletes a playlist.
     *
     * @param playlistId The ID of the playlist to delete.
     * @param profileId  The ID of the profile deleting the playlist.
     * @param language   Language for error messages.
     * @return True if playlist is deleted successfully.
     * @throws AppBadException If profile lacks permission to delete the playlist.
     */
    public Boolean delete(Integer playlistId, Integer profileId, AppLanguage language) {

        ProfileEntity profileEntity = profileService.get(profileId, language);
        PlaylistEntity entity = get(playlistId, language);

        if (profileEntity.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            playlistRepository.delete(entity.getId());
            return true;
        }

        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);

        if (!profileEntity.getId().equals(channelDTO.getProfileId())) {
            log.warn("Profile not found{}", profileId);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.allowed", language) + "-->>" + profileId);

        }
        playlistRepository.delete(entity.getId());
        return true;


    }

    /**
     * Retrieves a paginated list of playlists for administrators.
     *
     * @param page     The page number to retrieve (1-based index).
     * @param size     The number of playlists per page.
     * @param language Language for error messages.
     * @return A paginated list of playlists.
     */
    public PageImpl<PlaylistInfoDTO> pagination(Integer page, Integer size, AppLanguage language) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<PlaylistEntity> pages = playlistRepository.findAll(pageable);

        List<PlaylistEntity> content = pages.getContent();
        long totalElements = pages.getTotalElements();

        List<PlaylistInfoDTO> dtoList = new ArrayList<>();
        for (PlaylistEntity entity : content) {
            dtoList.add(toDTO(entity, language));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);

    }

    /**
     * Retrieves a list of playlists by ADMIN.
     *
     * @param profileId The profile ID of the user.
     * @param language  Language for error messages.
     * @return A list of playlists.
     */
    public List<PlaylistInfoDTO> getPlaylistByUserId(Integer profileId, AppLanguage language) {
        // Retrieve all playlists from repository
        List<PlaylistEntity> playlistEntities = playlistRepository.find();

        // Initialize a list to store playlist DTOs
        List<PlaylistInfoDTO> dtoList = new LinkedList<>();

        // Iterate through playlist entities
        for (PlaylistEntity playlistEntity : playlistEntities) {
            // Retrieve channel entity for the playlist
            ChannelEntity channelEntity = channelService.get(playlistEntity.getChannelId(), language);
            // Check if channel exists and profile ID matches
            if (channelEntity != null && channelEntity.getProfileId().equals(profileId)) {
                // Convert playlist entity to DTO and add to list
                dtoList.add(toDTO(playlistEntity, language));
            }
        }

        return dtoList;
    }


    /**
     * Retrieves a list of playlists by OWNER AND USER.
     *
     * @param profileId The profile ID of the owner/user.
     * @param language  Language for error messages.
     * @return A list of playlist short info DTOs.
     */
    public List<PlaylistShortInfoDTO> getPlaylistByOwner(Integer profileId, AppLanguage language) {
        // Retrieve all playlists ordered by order number
        Iterable<PlaylistEntity> all = playlistRepository.findAllByOrderByOrderNumberDesc();

        // Initialize a list to store playlist short info DTOs
        List<PlaylistShortInfoDTO> dtoList = new ArrayList<>();

        // Iterate through playlist entities
        for (PlaylistEntity entity : all) {
            // Retrieve channel entity for the playlist
            ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
            // Check if channel exists and profile ID matches
            if (channelEntity != null && channelEntity.getProfileId().equals(profileId)) {
                // Convert playlist entity to short info DTO and add to list
                dtoList.add(toShortDTO(entity, language));
            }
        }

        return dtoList;
    }

    /**
     * Retrieves a list of playlists by PUBLIC using the channel ID.
     *
     * @param channelId The channel ID.
     * @param language  Language for error messages.
     * @return A list of playlist short info DTOs.
     */
    public List<PlaylistShortInfoDTO> getPlaylistsByChannelId(String channelId, AppLanguage language) {
        // Retrieve playlists by channel ID ordered by order number
        List<PlaylistEntity> list = playlistRepository.findAllByChannelIdOrderByOrderNumberDesc(channelId);

        // Initialize a list to store playlist short info DTOs
        List<PlaylistShortInfoDTO> dtoList = new ArrayList<>();

        // Iterate through playlist entities
        for (PlaylistEntity entity : list) {
            // Convert playlist entity to short info DTO and add to list
            dtoList.add(toShortDTO(entity, language));
        }

        return dtoList;
    }


    /**
     * Retrieves detailed information about a playlist by PUBLIC.
     *
     * @param id       The playlist ID.
     * @param language Language for error messages.
     * @return Detailed information about the playlist.
     * @throws AppBadException if the playlist is not found.
     */
    public PlaylistDetailInfoDTO getDetailById(Integer id, AppLanguage language) {
        // Retrieve the playlist entity by ID if it is visible
        Optional<PlaylistEntity> optional = playlistRepository.findByIdAndVisibleTrue(id);

        // Throw an exception if the playlist is not found
        if (optional.isEmpty()) {
            log.warn("Playlist not found{}", id);
            throw new AppBadException(resourceBundleService.getMessage("playlist.not.found", language) + "-->" + id);
        }

        // Retrieve playlist videos and total view count
        List<PlaylistVideoEntity> list = playlistVideoRepository.findAllByPlaylistId(id);
        Integer totalViewCount = playlistVideoRepository.viewCount(id);

        // Get the playlist entity from the optional
        PlaylistEntity entity = optional.get();

        // Create a new PlaylistDetailInfoDTO object and set its attributes
        PlaylistDetailInfoDTO dto = new PlaylistDetailInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVideoCount(entity.getVideoCount());
        dto.setTotalViewCount(totalViewCount);
        dto.setLastUpdateDate(entity.getUpdatedDate());

        return dto;
    }


    /**
     * Converts a PlaylistEntity object to a PlaylistInfoDTO object.
     *
     * @param entity   The PlaylistEntity object to be converted.
     * @param language Language for error messages.
     * @return Converted PlaylistInfoDTO object.
     */
    public PlaylistInfoDTO toDTO(PlaylistEntity entity, AppLanguage language) {
        // Retrieve channel information
        ChannelDTO channel = channelService.getById(entity.getChannelId(), language);
        ProfileEntity profile = profileService.get(channel.getProfileId(), language);
        AttachEntity attachChannel = attachService.get(channel.getPhotoId(), language);
        AttachEntity attachProfile = attachService.get(profile.getAttachId(), language);

        // Create and populate ChannelDTO object
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(channel.getId());
        channelDTO.setName(channel.getName());
        AttachDTO channelPhoto = new AttachDTO();
        channelPhoto.setId(attachChannel.getId());
        channelPhoto.setUrl(attachChannel.getUrl());
        channelDTO.setPhoto(channelPhoto);

        // Create and populate ProfileDTO object
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profile.getId());
        profileDTO.setName(profile.getName());
        profileDTO.setSurname(profile.getSurname());
        AttachDTO profilePhoto = new AttachDTO();
        profilePhoto.setId(attachProfile.getId());
        profilePhoto.setUrl(attachProfile.getUrl());
        profileDTO.setAttach(profilePhoto);

        // Create and populate PlaylistInfoDTO object
        PlaylistInfoDTO dto = new PlaylistInfoDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setChannel(channelDTO);
        dto.setProfile(profileDTO);

        return dto;
    }


    /**
     * Converts a PlaylistEntity object to a PlaylistShortInfoDTO object.
     *
     * @param entity   The PlaylistEntity object to be converted.
     * @param language Language for error messages.
     * @return Converted PlaylistShortInfoDTO object.
     */
    public PlaylistShortInfoDTO toShortDTO(PlaylistEntity entity, AppLanguage language) {
        // Initialize PlaylistShortInfoDTO object
        PlaylistShortInfoDTO dto = new PlaylistShortInfoDTO();

        // Retrieve playlist videos and channel information
        Iterable<PlaylistVideoEntity> allVideos = playlistVideoRepository.findAllByPlaylistId(entity.getId());
        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);

        // Initialize list to store video DTOs
        List<VideoDTO> dtoList = new LinkedList<>();
        for (PlaylistVideoEntity playlistVideoEntity : allVideos) {
            // Retrieve video information for each playlist video
            VideoDTO videoDTO = new VideoDTO();
            VideoEntity videoEntity = videoService.get(playlistVideoEntity.getVideoId(), language);
            videoDTO.setId(videoEntity.getId());
            videoDTO.setTitle(videoEntity.getTitle());
            videoDTO.setDuration(videoEntity.getDuration());
            // Add video DTO to the list
            dtoList.add(videoDTO);
        }

        // Create and populate ChannelDTO object
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());

        // Populate PlaylistShortInfoDTO object
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoCount(entity.getVideoCount());
        dto.setChannel(channelDTO);
        dto.setVideos(dtoList);

        return dto;
    }


    /**
     * Retrieves a playlist entity by its ID.
     *
     * @param playlistId The ID of the playlist entity to retrieve.
     * @param language   Language for error messages.
     * @return The playlist entity with the specified ID.
     * @throws AppBadException if the playlist entity with the specified ID is not found.
     */
    public PlaylistEntity get(Integer playlistId, AppLanguage language) {
        return playlistRepository.findById(playlistId).orElseThrow(() -> {
            log.warn("Playlist not found{}", playlistId);
            return new AppBadException(resourceBundleService.getMessage("playlist.not.found", language) + "-->" + playlistId);
        });
    }


}
