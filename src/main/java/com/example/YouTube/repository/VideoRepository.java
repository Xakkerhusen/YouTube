package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoEntity;
import com.example.YouTube.mapper.VideoFullInfoMapper;
import com.example.YouTube.mapper.VideoShortInfoMapper;
import com.example.YouTube.mapper.VideoShortInfoPaginationMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@Transactional
public interface VideoRepository extends CrudRepository<VideoEntity, String>,
        PagingAndSortingRepository<VideoEntity, String> {

    @NotNull
    Page<VideoEntity> findAll(@NotNull Pageable paging);

    Page<VideoEntity> findByChannelId(Pageable paging, String id);

    List<VideoEntity> findByAttachId(String attachId);

    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id as previewAttachId," +
            " v.published_date as publishedDate,  v.view_countas viewCount , v.duration," +
            "       c.id as channelId, c.name as channelName, c.photo_id as photoId" +
            " from video as v " +
            "         inner join channel as c on c.id = v.channel_id " +
            "         inner join profile as p on p.id = c.profile_id " +
            " where v.id = ?1 order by c.created_date desc", nativeQuery = true)
    Page<VideoShortInfoMapper> getVideoShortInfoPagination(Pageable paging, Integer id);


    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id as previewAttachId," +
            " v.published_date as publishedDate,  v.view_count as viewCount, v.duration," +
            "       c.id as channelId, c.name as channelName, c.photo_id as photoId " +
            " from video as v " +
            "         inner join channel as c on c.id = v.channel_id " +
            "         inner join profile as p on p.id = c.profile_id " +
            " where v.id = ?1 order by c.created_date desc", nativeQuery = true)
    Optional<VideoShortInfoMapper> getVideoShortInfo(String id);


    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id as previewAttachId," +
            " v.published_date as publishedDate,  v.view_count as viewCount, v.duration," +
            " c.id as channelId, c.name as channelName, c.photo_id as photoId" +
            " from video as v " +
            " inner join channel as c on c.id = v.channel_id " +
            " inner join profile as p on p.id = c.profile_id ", nativeQuery = true)
    Page<VideoShortInfoMapper> getVideoShortInfoInteger(Pageable paging, Integer tagId);



    @Query(value = "select v.id as videoId, v.title, v.preview_attach_id as previewAttachId," +
            " v.published_date as publishedDate,  v.view_count as viewCount, " +
            " c.id as channelId, c.name as channelName, c.photo_id as photoId, " +
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


    @Query(value = "select v.id as videoId, v.title, v.description, v.published_date as publishedDate, v.view_count as viewCount," +
            " v.shared_count as sharedCount,v.like_count as likeCount, v.dislike_count as dislikeCount," +
            " v.preview_attach_id as previewAttachId , " +
            " c.id as channelId, c.name as channelName, c.photo_id as photoId, " +
            " p.id as profileId,p.name as profileName,p.surname, " +
            " ct.id as categoryId,ct.name as categoryName, " +
            " vm.status as emotion, " +
            " a.id as attachId,a.duration,a.url, " +
            " (select  cast(json_agg(temp_t) as text) " +
            " from (select json_build_object('id', t.id, 'name', t.tag_name) from tags as t" +
            " inner join video_tag as vt on vt.tag_id = t.id" +
            " inner join video vv on vv.id = vt.video_id" +
            " where vv.id = v.id) as temp_t) as tagListJson " +
            " from video as v " +
            " inner join channel as c on c.id = v.channel_id " +
            " inner join profile as p on p.id = c.profile_id " +
            " inner join category as ct on ct.id = v.category_id " +
            " inner join attach as a on a.id = v.attach_id " +
            " left join video_emotion as vm on vm.profile_id = p.id " +
            " where v.id = ?1 " +
            " order by v.created_date desc", nativeQuery = true)
    Optional<VideoFullInfoMapper> getVideFullInfo(String videoId);


}
