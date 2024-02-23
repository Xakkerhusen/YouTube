package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

@Transactional
public interface VideoRepository extends CrudRepository<VideoEntity,String>,
        PagingAndSortingRepository<VideoEntity, String> {

    @Modifying
    @Query("update VideoEntity set viewCount = viewCount + 1 where id = ?1")
    int increaseViewCount(String videoId);


    Page<VideoEntity> findAllByCategoryId(Pageable paging, Integer categoryId);

//    Page<VideoEntity> paginationVideoList(Pageable paging);
    @NotNull
    Page<VideoEntity> findAll(@NotNull Pageable paging);

    Page<VideoEntity> findByChannelId(Pageable paging, String id);

}
