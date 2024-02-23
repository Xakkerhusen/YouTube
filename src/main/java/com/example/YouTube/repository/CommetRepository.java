package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CommetRepository extends CrudRepository<CommentEntity,Integer>, PagingAndSortingRepository<CommentEntity,Integer > {


    @Query("from CommentEntity ce where ce.id=?1")
    Optional<CommentEntity> commentID(String integer);

    @Query("from CommentEntity ce where ce.profileId=?1")
    Page<CommentEntity> profileID(Integer integer, PageRequest pageable);
@Query("from CommentEntity ce where ce.profileId=?1")
    Optional<CommentEntity> profile(Integer profileID);

    @Query("from CommentEntity ce where ce.videoId=?1")
    List<CommentEntity> videoID(String profileID);

    @Query("from CommentEntity ce where ce.replyId=?1")
    Optional<CommentEntity> findByReplyId(String id);
}
