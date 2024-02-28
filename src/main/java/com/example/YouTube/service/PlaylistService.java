package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.PlaylistStatus;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.mapper.PlayListShortInfoMapper;
import com.example.YouTube.mapper.PlaylistInfoMapper;
import com.example.YouTube.repository.PlaylistRepository;
import com.example.YouTube.repository.PlaylistVideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);
        Integer profile = playlistRepository.findProfile(entity.getChannelId());
        if (!profileId.equals(profile)) {
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

//        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);
        Integer profile = playlistRepository.findProfile(entity.getChannelId());
        if (!profileId.equals(profile)) {
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

//        ChannelDTO channelDTO = channelService.getById(entity.getChannelId(), language);
        Integer profile = playlistRepository.findProfile(entity.getChannelId());

        if (!profileEntity.getId().equals(profile)) {
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
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PlaylistInfoMapper> playlistPage = playlistRepository.getPlayListInfo(pageable);
        List<PlaylistInfoDTO> playlistInfoDTOs = playlistPage.stream()
                .map(mapper -> mapToDTO(mapper, language))
                .collect(Collectors.toList());

        return new PageImpl<>(playlistInfoDTOs, pageable, playlistPage.getTotalElements());
    }

    /**
     * Retrieves a list of playlists by ADMIN.
     *
     * @param profileId The profile ID of the user.
     * @param language  Language for error messages.
     * @return A list of playlists.
     */

    public List<PlaylistInfoDTO> getPlaylistByUserId(Integer profileId, AppLanguage language) {
        // Retrieve playlist information from the repository
        ProfileEntity profileEntity = profileService.get(profileId, language);
        List<PlaylistInfoMapper> playlistInfoMappers = playlistRepository.getPlayListInfoList(profileEntity.getId());

        // Convert PlaylistInfoMapper objects to PlaylistInfoDTO objects
        List<PlaylistInfoDTO> playlistInfoDTOs = playlistInfoMappers.stream()
                .map(mapper -> mapToDTO(mapper, language))
                .collect(Collectors.toList());


        return playlistInfoDTOs;
    }


    /**
     * Retrieves a list of playlists by OWNER AND USER.
     *
     * @param profileId The profile ID of the owner/user.
     * @return A list of playlist short info DTOs.
     */
    public List<PlaylistShortInfoDTO> getPlaylistByOwner(Integer profileId) {
        // Biz profileId boyicha playlistlarni qaytarish uchun repositorydan foydalanamiz
        List<PlayListShortInfoMapper> playlistInfoMappers = playlistRepository.getPlayListShortInfoByOwner(profileId);

        // PlayListShortInfoMapper ni PlaylistShortInfoDTO ga o'tkazish
        List<PlaylistShortInfoDTO> playlistShortInfoDTOs = new ArrayList<>();
        for (PlayListShortInfoMapper mapper : playlistInfoMappers) {
            playlistShortInfoDTOs.add(mapToDTO(mapper));
        }
        return playlistShortInfoDTOs;
    }




    /**
     * Retrieves a list of playlists by PUBLIC using the channel ID.
     *
     * @param channelId The channel ID.
     * @return A list of playlist short info DTOs.
     */
    public List<PlaylistShortInfoDTO> getPlaylistsByChannelId(String channelId) {
        // Retrieve playlists by channel ID ordered by order number
        List<PlayListShortInfoMapper> playListShortInfoByChannel = playlistRepository.getPlayListShortInfoByChannel(channelId);

        // Initialize a list to store playlist short info DTOs
        List<PlaylistShortInfoDTO> dtoList = new ArrayList<>();

        // Iterate through playlist entities
        for (PlayListShortInfoMapper mapper : playListShortInfoByChannel) {
            // Convert playlist entity to short info DTO and add to list
            dtoList.add(mapToDTO(mapper));
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
     * @param mapper   The PlaylistInfoMapper object representing playlist information retrieved from the database.
     *                 This object contains raw data obtained from the database query.
     * @param language The language setting used for localization and fetching data in the desired language.
     *                 This parameter influences the fetching of attach entities such as channel and profile photos.
     * @return PlaylistInfoDTO object representing playlist information suitable for presentation or further processing.
     * This object contains transformed data ready to be used by the application.
     */

    private PlaylistInfoDTO mapToDTO(PlaylistInfoMapper mapper, AppLanguage language) {
        PlaylistInfoDTO dto = new PlaylistInfoDTO();
        dto.setId(mapper.getPlaylistId());
        dto.setName(mapper.getPlaylistName());
        dto.setDescription(mapper.getPlaylistDescription());
        dto.setStatus(PlaylistStatus.valueOf(mapper.getPlaylistStatus()));
        dto.setOrderNumber(mapper.getPlaylistOrderNumber());

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(mapper.getChannelId());
        channelDTO.setName(mapper.getChannelName());
        AttachDTO channelPhoto = new AttachDTO();
        if (mapper.getChannelPhotoId() != null) {
            AttachEntity attachEntity = attachService.get(mapper.getChannelPhotoId(), language);
            channelPhoto.setId(mapper.getChannelPhotoId());
            channelPhoto.setUrl(attachEntity.getUrl());
        }

        channelDTO.setPhoto(channelPhoto);
        dto.setChannel(channelDTO);

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(mapper.getProfileId());
        profileDTO.setName(mapper.getProfileName());
        profileDTO.setSurname(mapper.getProfileSurname());
        AttachDTO profilePhoto = new AttachDTO();
        if (mapper.getProfilePhotoId() != null) {
            AttachEntity attachEntity1 = attachService.get(mapper.getProfilePhotoId(), language);
            profilePhoto.setId(mapper.getProfilePhotoId());
            profilePhoto.setUrl(attachEntity1.getUrl());
        }
        profileDTO.setAttach(profilePhoto);
        dto.setProfile(profileDTO);

        return dto;
    }

    private PlaylistShortInfoDTO mapToDTO(PlayListShortInfoMapper mapper) {
        PlaylistShortInfoDTO dto = new PlaylistShortInfoDTO();
        dto.setPlaylistId(mapper.getPlaylistId());
        dto.setPlaylistName(mapper.getPlaylistName());
        dto.setPlaylistCreatedDate(mapper.getPlaylistCreatedDate());
        dto.setPlaylistVideoCount(mapper.getPlaylistVideoCount());

        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(mapper.getChannelId());
        channelDTO.setName(mapper.getChannelName());
        dto.setChannel(channelDTO);
        String playListJson = mapper.getPlayListJson();
        if (playListJson != null && !playListJson.isEmpty()) {
            dto.setPlayListJson(playListJson);
        }
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
