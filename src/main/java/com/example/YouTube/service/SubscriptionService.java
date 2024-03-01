package com.example.YouTube.service;

import com.example.YouTube.controller.ChangeSubscriptionNotificationDTO;
import com.example.YouTube.controller.ChangeSubscriptionStatusDTO;
import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.SubscriptionCreateDTO;
import com.example.YouTube.dto.SubscriptionInfoDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.ChannelEntity;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.entity.SubscriptionEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.Status;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.mapper.SubscriptionInfoMapper;
import com.example.YouTube.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ChannelService channelService;
    private final ResourceBundleService resourceBundleService;
    private final ProfileService profileService;
    private final AttachService attachService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               ChannelService channelService,
                               ResourceBundleService resourceBundleService,
                               ProfileService profileService,
                               AttachService attachService) {
        this.subscriptionRepository = subscriptionRepository;
        this.channelService = channelService;
        this.resourceBundleService = resourceBundleService;
        this.profileService = profileService;
        this.attachService = attachService;
    }


    public String create(Integer profileId, SubscriptionCreateDTO dto, AppLanguage language) {

        SubscriptionEntity entity = new SubscriptionEntity();
        ChannelEntity channelEntity = channelService.get(dto.getChannelId(), language);
        ProfileEntity profileEntity = profileService.get(profileId, language);

        entity.setChannelId(channelEntity.getId());
        entity.setNotificationType(dto.getNotificationType());
        entity.setProfileId(profileEntity.getId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(Status.ACTIVE);

        subscriptionRepository.save(entity);

        return "Success!!!";
    }

    public String changeSubscriptionStatus(Integer profileId, ChangeSubscriptionStatusDTO dto, AppLanguage language) {

        ProfileEntity profileEntity = profileService.get(profileId, language);
        Optional<SubscriptionEntity> subscription = subscriptionRepository.findByChannelIdAndProfileId(dto.getChannelId(), profileEntity.getId());


        if (subscription.isEmpty()) {
            log.warn("Subscription not found with this Channel");
            throw new AppBadException(resourceBundleService.getMessage("subscription.not.found", language));
        }

        SubscriptionEntity entity = subscription.get();

        entity.setStatus(dto.getStatus());

        subscriptionRepository.save(entity);

        return "Status has changed";
    }

    public String changeNotificationType(Integer profileId, ChangeSubscriptionNotificationDTO dto, AppLanguage language) {

        ProfileEntity profileEntity = profileService.get(profileId, language);
        Optional<SubscriptionEntity> subscription = subscriptionRepository.findByChannelIdAndProfileId(dto.getChannelId(), profileEntity.getId());

        if (subscription.isEmpty()) {
            log.warn("Subscription not found with this Channel");
            throw new AppBadException(resourceBundleService.getMessage("subscription.not.found", language));
        }

        SubscriptionEntity entity = subscription.get();

        entity.setNotificationType(dto.getNotificationType());

        subscriptionRepository.save(entity);

        return "Notification Type changed";
    }


    //bunda profileId SpringSecurityUtil.getCurrentUser() orqali kirib keladi
//    public List<SubscriptionInfoDTO> getInfo(Integer profileId, AppLanguage language){
//
//        ProfileEntity profileEntity = profileService.get(profileId, language);
//
//        List<SubscriptionEntity> list = subscriptionRepository.findByProfileId(profileEntity.getId());
//
//        List<SubscriptionInfoDTO> dtoList=new ArrayList<>();
//
//        for (SubscriptionEntity entity : list) {
//
//            dtoList.add(toDTOForUser(entity,language));
//        }
//
//        return dtoList;
//
//    }

    public List<SubscriptionInfoDTO> getInfo(Integer profileId, AppLanguage language) {

        List<SubscriptionInfoMapper> subscriptionInfo = subscriptionRepository.getSubscriptionInfo(profileId);

        List<SubscriptionInfoDTO> dtoList = new ArrayList<>();

        for (SubscriptionInfoMapper subscriptionInfoMapper : subscriptionInfo) {
            dtoList.add(mapToDTO(subscriptionInfoMapper,language));
        }
        return dtoList;
    }


    // bunda profile id @RequestParam orqali kirib keladi
    public List<SubscriptionInfoDTO> getInfoByAdmin(Integer profileId, AppLanguage language) {

        List<SubscriptionInfoMapper> subscriptionInfo = subscriptionRepository.getSubscriptionInfo(profileId);

        List<SubscriptionInfoDTO> dtoList = new ArrayList<>();

        for (SubscriptionInfoMapper subscriptionInfoMapper : subscriptionInfo) {
            dtoList.add(mapToDTOAdmin(subscriptionInfoMapper,language));
        }
        return dtoList;


    }
    public SubscriptionInfoDTO mapToDTO(SubscriptionInfoMapper mapper, AppLanguage language) {
        SubscriptionInfoDTO dto = new SubscriptionInfoDTO();

        dto.setId(mapper.getSubscriptionsId());
        ChannelDTO channelDTO=new ChannelDTO();
        ChannelEntity channelEntity = channelService.get(mapper.getChannelId(), language);
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());
        AttachEntity attachEntity = attachService.get(channelEntity.getPhotoId(), language);
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getUrl());
        channelDTO.setPhoto(attachDTO);
        dto.setChannel(channelDTO);
        dto.setNotificationType(mapper.getSubscriptionsNotificationType());

        return dto;
    }

    public SubscriptionInfoDTO mapToDTOAdmin(SubscriptionInfoMapper mapper, AppLanguage language) {
        SubscriptionInfoDTO dto = new SubscriptionInfoDTO();

        dto.setId(mapper.getSubscriptionsId());
        ChannelDTO channelDTO=new ChannelDTO();
        ChannelEntity channelEntity = channelService.get(mapper.getChannelId(), language);
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());
        AttachEntity attachEntity = attachService.get(channelEntity.getPhotoId(), language);
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getUrl());
        channelDTO.setPhoto(attachDTO);
        dto.setChannel(channelDTO);
        dto.setNotificationType(mapper.getSubscriptionsNotificationType());
        dto.setCreatedDate(mapper.getSubscriptionCreatedDate());

        return dto;
    }




}
