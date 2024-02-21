package com.example.YouTube.repository;


import com.example.YouTube.entity.EmailSentHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailSentHistoryRepository extends CrudRepository<EmailSentHistoryEntity, Integer>, PagingAndSortingRepository<EmailSentHistoryEntity,Integer> {
    @Query("SELECT count (s) from EmailSentHistoryEntity s where s.email =?1 and s.createdDate between ?2 and ?3")
    Long countSendEmail(String email, LocalDateTime from, LocalDateTime to);

}
