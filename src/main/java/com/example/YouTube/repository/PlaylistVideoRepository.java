package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.entity.PlaylistVideoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, Long> {

    List<PlaylistVideoEntity> findByVideoId(String videoId);

    void deleteByVideoIdAndPlaylistId(String videoId, Integer typeId);

}
