package com.example.YouTube.repository;

import com.example.YouTube.entity.AttachEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttachRepository extends CrudRepository<AttachEntity, String>,
        PagingAndSortingRepository<AttachEntity, String> {

    @NotNull
    Page<AttachEntity> findAll(@NotNull Pageable paging);


}