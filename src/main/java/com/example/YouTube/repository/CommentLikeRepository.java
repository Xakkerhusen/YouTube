package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentLikeEntity;
import com.example.YouTube.enums.LikeStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {
    @Query("from CommentLikeEntity where  commentId=?1 and profileId=?2")
    Optional<CommentLikeEntity> findTop1ByCommentId(Integer commentId, Integer profileId);


    @Transactional
    @Modifying
    @Query("update CommentLikeEntity set type=?2 , updatedDate=?3 where id=?1")
    Integer update(Integer id, String status, LocalDateTime now);

    @Query("from CommentLikeEntity where profileId=?1 and type='LIKE' order by createdDate desc ")
    List<CommentLikeEntity> getByProfileId(Integer profileId);

    List<CommentLikeEntity> findByProfileId(Integer id);
}
