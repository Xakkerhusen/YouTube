package com.example.YouTube.service;

import com.example.YouTube.config.CustomUserDetails;
import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.CreatChannelDTO;
import com.example.YouTube.dto.UpdateChannelDTO;
import com.example.YouTube.entity.ChannelEntity;
import com.example.YouTube.enums.Status;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    /**
     * This object is used to work with the database
     */
    @Autowired
    private ChannelRepository channelRepository;

    /**
     * This method is used to create a new channel
     */
    public String creat(CreatChannelDTO channelDTO) {
        Integer profileId = getUserDetails().getId();
        ChannelEntity entity = new ChannelEntity();
        entity.setName(channelDTO.getName());
        entity.setDescription(channelDTO.getDescription());
        entity.setBannerId(channelDTO.getBannerId());
        entity.setPhotoId(channelDTO.getPhotoId());
        entity.setProfileId(profileId);
        channelRepository.save(entity);
        return "Channel created";
    }

    /**
     * This method is used to update the value of
     * (name,description) variables in the Channel class
     */
    public ChannelDTO update(String channelId, UpdateChannelDTO channelDTO) {
        Integer profileId = getUserDetails().getId();
        Optional<ChannelEntity> optional = channelRepository.findByIdAndProfileId(channelId, profileId);
        if (optional.isEmpty()) throw new AppBadException("Channel not found");
        ChannelEntity entity = optional.get();
        if (!(channelDTO.getDescription() == null || channelDTO.getDescription().trim().isEmpty())) {
            entity.setDescription(channelDTO.getDescription());
        }
        if (!(channelDTO.getName() == null || channelDTO.getName().trim().isEmpty())) {
            entity.setName(channelDTO.getName());
        }
        channelRepository.save(entity);
        return toDTO(entity);
    }

    /**
     * This method is used to update the Channel Photo
     */
    public String updatePhoto(String channelId, String imageId) {
        Integer profileId = getUserDetails().getId();
        Optional<ChannelEntity> optional = channelRepository.findByIdAndProfileId(channelId, profileId);
        if (optional.isEmpty()) throw new AppBadException("Channel not found");
        // todo checking image
        channelRepository.changePhoto(channelId, imageId);
        return "Channel photo changed";
    }

    /**
     * This method is used to update the Channel Banner
     */
    public String updateBanner(String channelId, String imageId) {
        Integer profileId = getUserDetails().getId();
        Optional<ChannelEntity> optional = channelRepository.findByIdAndProfileId(channelId, profileId);
        if (optional.isEmpty()) throw new AppBadException("Channel not found");
        // todo checking image
        channelRepository.changeBanner(channelId, imageId);
        return "Channel photo changed";
    }

    /**
     * This method is used to get Channels by Pagination
     */
    public PageImpl<ChannelDTO> getByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ChannelEntity> all = channelRepository.findAll(pageable);
        return new PageImpl<>(toDTOList(all.getContent()), pageable, all.getTotalElements());
    }

    /**
     * This method is used to get Channel By Id
     */
    public ChannelDTO getById(String channelId) {
        Optional<ChannelEntity> optional = channelRepository.findById(channelId);
        if (optional.isEmpty()) throw new AppBadException("Channel not found");
        return toDTO(optional.get());
    }

    /**
     * This method is used to update the Channel Status
     */
    public String updateStatus(String channelId, Status status) {
        Integer profileId = getUserDetails().getId();
        Optional<ChannelEntity> optional = channelRepository.findByIdAndProfileId(channelId, profileId);
        if (optional.isEmpty()) throw new AppBadException("Channel not found");
        channelRepository.changeStatus(channelId,status);
        return "Status is changed";
    }

    /**
     * This method is used to get the list of channels
     */
    public List<ChannelDTO> getChannelList() {
        return toDTOList(channelRepository.findByProfileId(getUserDetails().getId()));
    }

    /**
     * This method is used to get UserDetails
     */
    private static CustomUserDetails getUserDetails() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * This method is used to load object data from Entity List to DTO List
     */
    private List<ChannelDTO> toDTOList(List<ChannelEntity> entityList) {
        List<ChannelDTO> dtoList = new LinkedList<>();
        for (ChannelEntity entity : entityList) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    /**
     * This method is used to load object data from Entity class to DTO class
     */
    private ChannelDTO toDTO(ChannelEntity entity) {
        ChannelDTO dto = new ChannelDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setPhotoId(entity.getPhotoId());
        dto.setBannerId(entity.getBannerId());
        dto.setProfileId(entity.getProfileId());
        return dto;
    }

}
