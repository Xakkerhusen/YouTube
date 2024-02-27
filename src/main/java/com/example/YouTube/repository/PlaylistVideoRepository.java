package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistVideoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, Integer> {
    List<PlaylistVideoEntity> findAllByPlaylistId(Integer id);





    @Query(value = "select sum(v.view_count) from playlist_video p " +
            "inner join video v on p.video_id=v.id where p.playlist_id = ?1",
            nativeQuery = true)
    Integer viewCount(Integer playlistId);


    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, String videoId);

}
