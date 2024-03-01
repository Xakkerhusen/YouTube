package com.example.YouTube.controller;

import com.example.YouTube.dto.CreateReportDTO;
import com.example.YouTube.dto.ReportInfoDTO;
import com.example.YouTube.entity.ReportEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.ReportService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/create")
    @Operation(summary = "API create report",description = "through this API, the user can create a report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody CreateReportDTO dto,
                                    @RequestHeader(value = "Accept-Language") AppLanguage language) {
        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(reportService.create(profileID, dto, language));
    }

    @GetMapping("/adm/paginationReport")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API pagination report",description = "through this API, ADMIN can page reports written by users.")
    public ResponseEntity<PageImpl<ReportInfoDTO>> getReportListPagination(
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "page") Integer page,
            @RequestHeader(value = "Accept-Language") AppLanguage language) {

        return ResponseEntity.ok(reportService.getList(size, page, language));
    }


    @DeleteMapping("/adm/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API remove report",description = "through this API, ADMIN can delete reports written by users.")
    public ResponseEntity<?> removeReport(@PathVariable(value = "id") Integer reportId,
                                          @RequestHeader(value = "Accept-Language") AppLanguage lan) {
        return ResponseEntity.ok(reportService.removeReport(reportId, lan));
    }

    @GetMapping("/adm/reportList/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API  getReportList",description = "through this API, ADMIN can view user-written reports.")
    public ResponseEntity<List<ReportInfoDTO>> getReportList(
                                            @PathVariable(value = "id") Integer profileId,
                                            @RequestHeader(value = "Accept-Language") AppLanguage language) {

        return ResponseEntity.ok(reportService.getReportList(profileId, language));
    }


}
