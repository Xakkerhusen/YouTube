package com.example.YouTube.repository;

import com.example.YouTube.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity,Long> {

    Optional<SubscriptionEntity> findByChannelIdAndProfileId(String channelId,Integer profileId);

    @Query("from SubscriptionEntity WHERE profileId = ?1 and status = 'ACTIVE'")
    List<SubscriptionEntity> findByProfileId(Integer profileId);
}
