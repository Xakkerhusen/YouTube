package com.example.YouTube.repository;

import com.example.YouTube.entity.TagNameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TagNameRepository extends CrudRepository<TagNameEntity,Integer> {

    Optional<TagNameEntity> findByTagName(String tagName);
}
