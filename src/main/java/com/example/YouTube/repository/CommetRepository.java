package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

public interface CommetRepository extends CrudRepository<CommentEntity,Integer> {
}
