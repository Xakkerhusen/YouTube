package com.example.YouTube.repository;

import com.example.YouTube.dto.ChannelDTO;
import com.example.YouTube.entity.ChannelEntity;
import com.example.YouTube.enums.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends CrudRepository<ChannelEntity, String>, PagingAndSortingRepository<ChannelEntity, String> {
    Optional<ChannelEntity> findByIdAndProfileId(String channelId, Integer profileId);

    @Transactional
    @Modifying
    @Query("update ChannelEntity set photoId=?2 where id=?1")
    void changePhoto(String id, String photoId);
    @Transactional
    @Modifying
    @Query("update ChannelEntity set bannerId=?2 where id=?1")
    void changeBanner(String channelId, String imageId);

    List<ChannelEntity> findByProfileId(Integer id);
    @Transactional
    @Modifying
    @Query("update ChannelEntity set status=?2 where id=?1")
    void changeStatus(String channelId, Status status);
}
