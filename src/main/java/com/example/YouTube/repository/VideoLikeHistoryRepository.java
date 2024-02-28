package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentLikeEntity;
import com.example.YouTube.entity.VideoLikeEntity;
import com.example.YouTube.entity.VideoLikeHistoryEntity;
import com.example.YouTube.mapper.VideolikeInfoMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeHistoryRepository extends CrudRepository<VideoLikeHistoryEntity, Integer> {
    @Transactional
    @Modifying
    @Query("update VideoLikeHistoryEntity set visible=false ,deletedDate=current_timestamp where id=?1")
    void deleteVideoLike(Integer id);

    @Transactional
    @Modifying
    @Query("update VideoLikeHistoryEntity set type=?2,updatedDate=current_timestamp where id=?1")
    void updateVideoLike(Integer id, String type);


    @Query(value = "select vl.id as videoLikeId, v.id as videoId, v.title, ch.id as channelId, ch.name, v.duration, v.preview_attach_id as previewAttachId" +
            " from video_like_history vl " +
            "         inner join video v on vl.video_id = v.id" +
            "         inner join channel ch on v.channel_id = ch.id" +
            " where vl.profile_id =?1 and vl.visible = true ",nativeQuery = true)
    List<VideolikeInfoMapper> getByProfileId(Integer profileId);

}
