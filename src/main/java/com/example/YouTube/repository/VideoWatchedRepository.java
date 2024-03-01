package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoWatchedEntity;
import org.springframework.data.repository.CrudRepository;

public interface VideoWatchedRepository extends CrudRepository<VideoWatchedEntity,String> {

}
