package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.mapper.VideoShortInfoMapper;
import com.example.YouTube.mapper.VideoShortInfoPaginationMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Transactional
public interface VideoRepository extends CrudRepository<VideoEntity, String>,
        PagingAndSortingRepository<VideoEntity, String> {

    @Modifying
    @Query("update VideoEntity set viewCount = viewCount + 1 where id = ?1")
    int increaseViewCount(String videoId);


    Page<VideoEntity> findAllByCategoryId(Pageable paging, Integer categoryId);

    //    Page<VideoEntity> paginationVideoList(Pageable paging);
    @NotNull
    Page<VideoEntity> findAll(@NotNull Pageable paging);

    Page<VideoEntity> findByChannelId(Pageable paging, String id);


    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id, v.published_date,  v.view_count,v.duration," +
            "       c.id as channelId, c.name as channelName, c.photo_id" +
            " from video as v " +
            "         inner join channel as c on c.id = v.channel_id " +
            "         inner join profile as p on p.id = c.profile_id " +
            " where v.id = ?1 order by c.created_date desc", nativeQuery = true)
    Page<VideoShortInfoMapper> getVideoShortInfo(Pageable paging, Integer id);

    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id, v.published_date,  v.view_count,v.duration," +
            "       c.id as channelId, c.name as channelName, c.photo_id" +
            " from video as v " +
            "         inner join channel as c on c.id = v.channel_id " +
            "         inner join profile as p on p.id = c.profile_id " +
            " where v.id = ?1 order by c.created_date desc", nativeQuery = true)
    Page<VideoShortInfoMapper> getVideoShortInfoString(Pageable paging, String tagName);
    //   VidePlayListInfo(id,title, preview_attach(id,url), view_count,
    //                       published_date,duration)


    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id, v.published_date,  v.view_count, " +
            " c.id as channelId, c.name, c.photo_id, " +
            " p.id as profileId,p.name as profileName,p.surname, " +
            " (select  cast(json_agg(temp_t) as text) " +
            " from (select json_build_object('id', pl.id, 'name', pl.name) from playlist as pl " +
            " inner join playlist_video as plv on plv.playlist_id = pl.id " +
            " inner join video vv on vv.id = plv.video_id " +
            " where vv.id = v.id) as temp_t) as playListJson " +
            " from video as v " +
            " inner join channel as c on c.id = v.channel_id " +
            " inner join profile as p on p.id = c.profile_id " +
            " order by v.created_date desc", nativeQuery = true)
    Page<VideoShortInfoPaginationMapper> getVideoListForAdmin(Pageable pageable);

    //  (VideShortInfo + owner (is,name,surname) + [playlist (id,name))]


    List<VideoEntity> findByAttachId(String attachId);


}
