package com.example.YouTube.repository;

import com.example.YouTube.entity.CommentEntity;
import com.example.YouTube.entity.ReportEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity,Integer> , PagingAndSortingRepository<ReportEntity,Integer > {
    @Query("from ReportEntity re where re.profileID=?1")
    List<ReportEntity> getReportProfileID(Integer profileId);
}
