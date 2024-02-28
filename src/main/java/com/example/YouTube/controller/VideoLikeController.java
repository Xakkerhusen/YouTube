package com.example.YouTube.controller;

import com.example.YouTube.dto.CreatedLikeDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.CommentLikeService;
import com.example.YouTube.service.VideoLikeService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Video Like API list", description = "API list for Video Like")
@RestController
@RequestMapping("/videoLike")
public class VideoLikeController {
    @Autowired
    private VideoLikeService videoLikeService;

    @Operation(summary = "API for create", description = "this api is used to create video like")
    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    public ResponseEntity<?> create(@PathVariable("id") String videoId,
                                    @RequestBody CreatedLikeDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Create video like ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(videoLikeService.create(videoId, profileId, dto, language));
    }

    @Operation(summary = "API for userLikedVideoList", description = "this api is used to get User Liked Video List")
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> userLikedVideoList(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("get User Liked Comment List ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(videoLikeService.userLikedVideoList(profileId, language));
    }

    @Operation(summary = "API for Get userLikedVideoListByUserId", description = "this api is used to  Get User LikeVideo List By UserId")
    @GetMapping("adm/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> userLikedVideoListByUserId(@PathVariable("id")Integer id,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Get User Like Video List By UserId ");
        SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok(videoLikeService.userLikedVideoListByUserId(id, language));
    }

}
