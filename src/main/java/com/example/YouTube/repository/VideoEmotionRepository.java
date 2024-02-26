package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoEmotionEntity;
import com.example.YouTube.enums.LikeStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VideoEmotionRepository extends CrudRepository<VideoEmotionEntity,Integer> {


    Optional<VideoEmotionEntity> findByVideoIdAndProfileId(String videoId, Integer profileId);

//    @Transactional
//    @Modifying
//    @Query("update VideoEmotionEntity set visible = ?2 where id = ?1")
//    void updateVisible(Integer id,boolean visible);


    @Transactional
    @Modifying
    @Query("update VideoEmotionEntity set status = ?2 where id = ?1")
    void updateStatus(Integer id, LikeStatus newStatus);

    @Query("from VideoEmotionEntity where profileId = ?1 and status = 'LIKE'")
    List<VideoEmotionEntity> findByProfileIdOrderByCreatedDate(Integer profileId);

}
