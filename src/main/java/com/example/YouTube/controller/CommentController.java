package com.example.YouTube.controller;

import com.example.YouTube.config.SpringSecurityConfig;
import com.example.YouTube.dto.CommentDTO;
import com.example.YouTube.dto.CommentInfoDTO;
import com.example.YouTube.dto.CommentPaginationDTO;
import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.CommentService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "API pagination comment(ADMIN)", description = "this api is used to pagination comment(ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> pagination(@RequestParam(value = "size") Integer size,
                                        @RequestParam(value = "page") Integer page) {
        return ResponseEntity.ok(service.pagination(size, page));
    }


    @GetMapping("/adm/paginationProfileID")
    @Operation(summary = "API pagination comment profileID(ADMIN)", description = "this api is used to pagination comment(ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageImpl<CommentPaginationDTO>> paginationProfileID(
            @RequestParam(value = "size", defaultValue = "1") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "id") Integer profileID,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(service.paginationProfile(profileID, size, page, language));
    }


    @GetMapping("/paginationProfileID")
    @Operation(summary = "API pagination comment profileID(USER)", description = "this api is used to pagination comment(USER)")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageImpl<CommentPaginationDTO>> paginationProile(
            @RequestParam(value = "size", defaultValue = "1") Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        Integer profileID = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(service.paginationProfile(profileID, size, page, language));
    }

    @GetMapping("/getListVideo/{id}")
    @Operation(summary = "API list comment videoID", description = "This API brings comments written to it via videoID")
    public ResponseEntity<List<CommentInfoDTO>> getListVideoID(@PathVariable(value = "id") String videoId,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(service.getListVideo(videoId, language));
    }


    @GetMapping("/replyList/{id}")
    public ResponseEntity<List<CommentInfoDTO>> replyID(@PathVariable(value = "id") String commentID,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(service.getreplyIdList(commentID, language));

    }

}
