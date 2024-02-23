package com.example.YouTube.controller;

import com.example.YouTube.dto.FilterEmailHistoryDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.EmailHistoryService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Email History API list", description = "API list for Email History")
@RestController
@RequestMapping("emailHistory")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @Operation(summary = "Api for getAllEmailHistory", description = "this api used for get all Email History")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adm")
    public ResponseEntity<?> getAllEmailHistory(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "siza", defaultValue = "2") Integer size,
                                                @RequestHeader(value = "Accept-Language",defaultValue = "UZ")AppLanguage language) {
        SpringSecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdDate");
        return ResponseEntity.ok(emailHistoryService.getAllEmailHistory(pageable,language));
    }

    @Operation(summary = "Api for getAllEmailHistoryByEmail", description = "this api used for get all Email History By Eamil")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/any/{email}")
    public ResponseEntity<?> getAllEmailHistoryByEmail(@PathVariable("email") String email,
                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @RequestParam(value = "siza", defaultValue = "2") Integer size,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "UZ")AppLanguage language) {
        SpringSecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdDate");
        return ResponseEntity.ok(emailHistoryService.getAllEmailHistoryByEmail(email,pageable,language));
    }
    @Operation(summary = "Api for filter", description = "this api used for get all Email History By Eamil")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/adm/filter")
    public ResponseEntity<?> filter(@RequestBody FilterEmailHistoryDTO dto,
                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "siza", defaultValue = "2") Integer size,
                                    @RequestHeader(value = "Accept-Language",defaultValue = "UZ")AppLanguage language) {
        SpringSecurityUtil.getCurrentUser();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdDate");
        return ResponseEntity.ok(emailHistoryService.filter(dto,pageable,language));
    }

}
