package com.example.YouTube.repository;

import com.example.YouTube.entity.VideoEntity;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<VideoEntity,String> {
}
