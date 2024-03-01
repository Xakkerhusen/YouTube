package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoTagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Transactional
public interface VideoTagRepository extends CrudRepository<VideoTagEntity, String>,
        PagingAndSortingRepository<VideoTagEntity, String> {
    @Transactional
    @Modifying
    @Query("update VideoTagEntity set visible=false where videoId=?1 and tagId=?2")
    Integer deleteByVideoIdAndTagId(String videoId, Integer tagId);

    @Query("from VideoTagEntity where visible=true and videoId=?1")
    Iterable<VideoTagEntity> findByVideoId(String videoId);

    @Query("from VideoTagEntity where visible=true and videoId=?1")
    List<VideoTagEntity> findByVideoIdList(String videoId);

    Page<VideoTagEntity> findByTagId(Pageable paging, Integer tagId);

    @Query("delete from VideoTagEntity where tagName = ?1 and videoId = ?2")
    void deleteByTagNameAndVideoId(String tagVideo, String id);

    @Transactional
    @Modifying
    @Query("delete from VideoTagEntity where videoId = ?1 and tagName = ?2")
    void deleteByVideoIdAndTagName(String videoId, String tagName);

}
