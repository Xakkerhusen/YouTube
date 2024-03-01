package com.example.YouTube.repository;

import com.example.YouTube.entity.SubscriptionEntity;
import com.example.YouTube.mapper.SubscriptionInfoMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity,Long> {

    Optional<SubscriptionEntity> findByChannelIdAndProfileId(String channelId,Integer profileId);

    @Query("from SubscriptionEntity WHERE profileId = ?1 and status = 'ACTIVE'")
    List<SubscriptionEntity> findByProfileId(Integer profileId);

    @Query(value = "select s.id as subscriptionsId,\n" +
            "       c.id as channelId,\n" +
            "       c.photo_id as channelPhotoId,\n" +
            "       s.notification_type as subscriptionsNotificationType,\n" +
            "       s.created_date as subscriptionCreatedDate\n" +
            "from subscriptions s\n" +
            "inner join public.channel c on c.id = s.channel_id\n" +
            "inner join public.profile p on p.id = s.profile_id\n" +
            "where p.id=1 and s.status='ACTIVE';", nativeQuery = true)
    List<SubscriptionInfoMapper> getSubscriptionInfo(Integer profileId);
}
