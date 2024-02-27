package com.example.YouTube.controller;

import com.example.YouTube.dto.CreatedCommentLikeDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.CommentLikeService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.opencv.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Comment Like API list", description = "API list for Comment Like")
@RestController
@RequestMapping("/commentLike")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @Operation(summary = "API for create", description = "this api is used to create comment like")
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    public ResponseEntity<?> create(@PathVariable("id") Integer commentId,
                                    @RequestBody CreatedCommentLikeDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Create comment like ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentLikeService.create(commentId, profileId, dto, language));
    }

    @Operation(summary = "API for userLikedCommentList", description = "this api is used to get User Liked Comment List")
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userLikedCommentList(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("get User Liked Comment List ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(commentLikeService.userLikedCommentList(profileId, language));
    }

    @Operation(summary = "API for Get userLikedCommentListByUserId", description = "this api is used to  Get User LikedComment List By UserId")
    @GetMapping("adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> userLikedCommentListByUserId(@PathVariable("id")Integer id,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Get User LikedComment List By UserId ");
        SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok(commentLikeService.userLikedCommentListByUserId(id, language));
    }

}
