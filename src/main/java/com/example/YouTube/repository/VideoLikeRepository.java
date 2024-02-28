package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentLikeEntity;
import com.example.YouTube.entity.VideoLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity, Integer> {
    @Query("from VideoLikeEntity where  videoId=?1 and profileId=?2")
    Optional<VideoLikeEntity> findTop1ByVideoId(String commentId, Integer profileId);

    @Transactional
    @Modifying
    @Query("update VideoLikeEntity set type=?2  where id=?1")
    Integer update(Integer id, String status);

//    @Query("from CommentLikeEntity where profileId=?1 and type='LIKE' order by createdDate desc ")
//    List<CommentLikeEntity> getByProfileId(Integer profileId);
//
//    List<CommentLikeEntity> findByProfileId(Integer id);
}
