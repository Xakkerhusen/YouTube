package com.example.YouTube.service;

import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.dto.SubscriptionCreateDTO;
import com.example.YouTube.dto.SubscriptionInfoDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.ChannelEntity;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.entity.SubscriptionEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
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


    public String create(Integer profileId,SubscriptionCreateDTO dto, AppLanguage language){

        SubscriptionEntity entity=new SubscriptionEntity();
        ChannelEntity channelEntity = channelService.get(dto.getChannelId(), language);
        ProfileEntity profileEntity = profileService.get(profileId, language);

        entity.setChannelId(channelEntity.getId());
        entity.setNotificationType(dto.getNotificationType());
        entity.setProfileId(profileEntity.getId());
        entity.setCreatedDate(LocalDateTime.now());

        subscriptionRepository.save(entity);

        return "Success!!!";
    }

    public String changeSubscriptionStatus(Integer profileId,SubscriptionCreateDTO dto,AppLanguage language){

        ProfileEntity profileEntity = profileService.get(profileId, language);
        Optional<SubscriptionEntity> subscription = subscriptionRepository.findByChannelIdAndProfileId(dto.getChannelId(),profileEntity.getId());


        if (subscription.isEmpty()){
            log.warn("Subscription not found with this Channel");
            throw new AppBadException(resourceBundleService.getMessage("subscription.not.found",language));
        }

        SubscriptionEntity entity = subscription.get();

        entity.setStatus(dto.getStatus());

        subscriptionRepository.save(entity);

        return "Status has changed";
    }

    public String changeNotificationType(Integer profileId,SubscriptionCreateDTO dto,AppLanguage language){

        ProfileEntity profileEntity = profileService.get(profileId, language);
        Optional<SubscriptionEntity> subscription = subscriptionRepository.findByChannelIdAndProfileId(dto.getChannelId(),profileEntity.getId());

        if (subscription.isEmpty()){
            log.warn("Subscription not found with this Channel");
            throw new AppBadException(resourceBundleService.getMessage("subscription.not.found",language));
        }

        SubscriptionEntity entity = subscription.get();

        entity.setNotificationType(dto.getNotificationType());

        subscriptionRepository.save(entity);

        return "Status has changed";
    }


    //bunda profileId SpringSecurityUtil.getCurrentUser() orqali kirib keladi
    public List<SubscriptionInfoDTO> getInfo(Integer profileId, AppLanguage language){

        ProfileEntity profileEntity = profileService.get(profileId, language);

        List<SubscriptionEntity> list = subscriptionRepository.findByProfileId(profileEntity.getId());

        List<SubscriptionInfoDTO> dtoList=new ArrayList<>();

        for (SubscriptionEntity entity : list) {

            dtoList.add(toDTOForUser(entity,language));
        }

        return dtoList;


    }

    // bunda profile id @RequestParam orqali kirib keladi
    public List<SubscriptionInfoDTO> getInfoByAdmin(Integer profileId, AppLanguage language){

        ProfileEntity profileEntity = profileService.get(profileId, language);

        List<SubscriptionEntity> list = subscriptionRepository.findByProfileId(profileEntity.getId());

        List<SubscriptionInfoDTO> dtoList=new ArrayList<>();

        for (SubscriptionEntity entity : list) {

            dtoList.add(toDTOForAdmin(entity,language));
        }

        return dtoList;


    }


    public SubscriptionInfoDTO toDTOForUser(SubscriptionEntity entity,AppLanguage language){
        SubscriptionInfoDTO dto=new SubscriptionInfoDTO();

        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
        AttachEntity attachEntity = attachService.get(channelEntity.getPhotoId(), language);

        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());

        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getUrl());
        channelDTO.setPhoto(attachDTO);
        dto.setId(entity.getId());
        dto.setChannel(channelDTO);
        dto.setNotificationType(entity.getNotificationType());

        return dto;

    }

    public SubscriptionInfoDTO toDTOForAdmin(SubscriptionEntity entity,AppLanguage language){
        SubscriptionInfoDTO dto=new SubscriptionInfoDTO();

        ChannelEntity channelEntity = channelService.get(entity.getChannelId(), language);
        AttachEntity attachEntity = attachService.get(channelEntity.getPhotoId(), language);

        ChannelDTO channelDTO=new ChannelDTO();
        channelDTO.setId(channelEntity.getId());
        channelDTO.setName(channelEntity.getName());

        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(attachEntity.getId());
        attachDTO.setUrl(attachEntity.getUrl());
        channelDTO.setPhoto(attachDTO);
        dto.setId(entity.getId());
        dto.setChannel(channelDTO);
        dto.setNotificationType(entity.getNotificationType());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;

    }



}
