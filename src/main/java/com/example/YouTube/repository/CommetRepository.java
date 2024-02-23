package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CommetRepository extends CrudRepository<CommentEntity,Integer>, PagingAndSortingRepository<CommentEntity,Integer > {


    @Query("from CommentEntity ce where ce.id=?1")
    Optional<CommentEntity> commentID(String integer);
}
