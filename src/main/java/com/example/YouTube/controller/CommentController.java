package com.example.YouTube.controller;

import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.CommentService;
import com.example.YouTube.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<?> create(@Valid @RequestBody() CreateCommentDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {

        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(service.create(dto, profileID, language));
    }

}
