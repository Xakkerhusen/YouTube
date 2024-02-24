package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity, Integer> {


    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible=false where id=?1")
    void delete(Integer id);

    Page<PlaylistEntity> findAll(Pageable pageable);

    @Query("from PlaylistEntity where visible=true")
    List<PlaylistEntity> find();
}
