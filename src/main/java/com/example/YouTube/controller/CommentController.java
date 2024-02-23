package com.example.YouTube.controller;

import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.CommentService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService service;

    @PostMapping("/create")
    @Operation(summary = "API create comment", description = "this api is used to create comment")
    @PreAuthorize("hasRole('USER')")

    public ResponseEntity<?> create(@Valid @RequestBody() CreateCommentDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {

        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(service.create(dto, profileID, language));
    }


    @PostMapping("/update/{id}")
    @Operation(summary = "API update comment", description = "this api is used to update comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@PathVariable(value = "id") String commentId,
                                    @Valid @RequestBody() CreateCommentDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {

        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(service.update(dto, profileID, language, commentId));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "API delete comment", description = "this api is used to delete comment")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String commentId,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {

        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(service.delete(profileID, language, commentId));
    }


    @GetMapping("/adm/pagination")
    @Operation(summary = "API pagination comment(ADMIN)", description = "")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> pagination(@RequestParam(value = "size") Integer size,
                                        @RequestParam(value = "page") Integer page) {

        return ResponseEntity.ok(service.pagination(size, page));
    }

}
