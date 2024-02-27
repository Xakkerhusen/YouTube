package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity, Integer> {


    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible=false where id=?1")
    void delete(Integer id);

    Page<PlaylistEntity> findAll(Pageable pageable);

    @Query("from PlaylistEntity where visible=true order by orderNumber desc ")
    List<PlaylistEntity> find();

    List<PlaylistEntity> findAllByChannelIdOrderByOrderNumberDesc(String channelId);


    //    @Query("from PlaylistEntity where visible=true ")
    Optional<PlaylistEntity> findByIdAndVisibleTrue(Integer id);

    List<PlaylistEntity> findAllByOrderByOrderNumberDesc();

}
