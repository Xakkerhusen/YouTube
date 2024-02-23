package com.example.YouTube.repository;


import com.example.YouTube.dto.FilterEmailHistoryDTO;
import com.example.YouTube.dto.PaginationResultDTO;
import com.example.YouTube.entity.EmailSentHistoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomRepository {
    @Autowired
    private EntityManager entityManager;


    public PaginationResultDTO<EmailSentHistoryEntity> emailHistoryFilter(FilterEmailHistoryDTO filter, Pageable pageable) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if (filter.getEmail() != null) {
            builder.append(" and email=:email ");
            params.put("email", filter.getEmail());
        }
        if (filter.getFromDate() != null && filter.getToDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filter.getToDate(), LocalTime.MAX);
            builder.append(" and createdDate  between :fromDate and :toDate");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        } else if (filter.getFromDate() != null) {
            LocalDateTime fromDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MIN);
            LocalDateTime toDate = LocalDateTime.of(filter.getFromDate(), LocalTime.MAX);
            builder.append(" and createdDate between :fromDate and :toDate ");
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
        } else if (filter.getToDate() != null) {
            LocalDateTime toDate = LocalDateTime.of(filter.getToDate(), LocalTime.MAX);
            builder.append(" and createdDate <= :toDate");
            params.put("toDate", toDate);
        }
        StringBuilder selectBuilder = new StringBuilder(" from EmailSentHistoryEntity e where 1=1  ");
        selectBuilder.append(builder);
        selectBuilder.append(" order by createdDate desc ");

        StringBuilder countBuilder = new StringBuilder(" select count(e) from EmailSentHistoryEntity e where 1=1  ");
        countBuilder.append(builder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setMaxResults(pageable.getPageSize());
        selectQuery.setFirstResult((int) pageable.getOffset());

        Query countQuery = entityManager.createQuery(countBuilder.toString());

        for (Map.Entry<String, Object> param : params.entrySet()) {
            selectQuery.setParameter(param.getKey(), param.getValue());
            countQuery.setParameter(param.getKey(), param.getValue());
        }
        List<EmailSentHistoryEntity> enitityList = selectQuery.getResultList();
        Long totalElement = (Long) countQuery.getSingleResult();
        return new PaginationResultDTO<>(totalElement, enitityList);
    }



}
