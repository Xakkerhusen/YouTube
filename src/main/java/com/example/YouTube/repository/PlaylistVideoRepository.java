package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistVideoEntity;
import jakarta.transaction.Transactional;
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


}
