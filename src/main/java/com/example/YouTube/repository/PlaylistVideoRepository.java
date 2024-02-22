package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.entity.PlaylistVideoEntity;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideoEntity, Long> {
}
