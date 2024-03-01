package com.example.YouTube.repository;

import com.example.YouTube.dto.TagShortDTO;
import com.example.YouTube.entity.TagNameEntity;
import com.example.YouTube.mapper.TagsMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TagNameRepository extends CrudRepository<TagNameEntity, Integer> {

    Optional<TagNameEntity> findByTagName(String tagName);


    @Query(value = "select t.id,t.tag_name from tags as t", nativeQuery = true)
    List<TagsMapper> getAllTagNameIds();

}
