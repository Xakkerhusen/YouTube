package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistVideoEntity;

import jakarta.transaction.Transactional;

import com.example.YouTube.mapper.PlaylistVideoInfoMapper;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, Long> {

    List<PlaylistVideoEntity> findByVideoId(String videoId);

    List<PlaylistVideoEntity> findByVideoIdAndPlaylistId(String videoId,Integer playlistId);

    @Transactional
    void deleteByVideoIdAndPlaylistId(String videoId, Integer typeId);


    @Query("from PlaylistVideoEntity where id in ALL (?1)")
    List<PlaylistVideoEntity> findByPlaylistId(List<Integer> playlistVideo);

import java.util.Optional;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, Integer> {
    List<PlaylistVideoEntity> findAllByPlaylistId(Integer id);





    @Query(value = "select sum(v.view_count) from playlist_video p " +
            "inner join video v on p.video_id=v.id where p.playlist_id = ?1",
            nativeQuery = true)
    Integer viewCount(Integer playlistId);


    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, String videoId);

    @Query(value = "select p.id as playlistId,\n" +
            "       v.id as videoId,\n" +
            "       v.preview_attach_id as videoPreviewAttachId,\n" +
            "       v.title as videoTitle,\n" +
            "       v.duration as videoDuration,\n" +
            "       c.id as channelId,\n" +
            "       c.name as channelName,\n" +
            "       pv.created_date as playlistVideoCreatedDate,\n" +
            "       pv.oreder_number as playlistVideoOrderNumber\n" +
            "           from\n" +
            "        playlist p\n" +
            "inner join public.playlist_video pv on p.id = pv.playlist_id\n" +
            "inner join public.video v on v.id = pv.video_id\n" +
            "inner join public.channel c on c.id = p.channel_id\n" +
            "where p.id=?1 and v.video_status='PUBLIC'",nativeQuery = true)

    List<PlaylistVideoInfoMapper> getPlaylistVideoInfo(Integer playlistId);

}
