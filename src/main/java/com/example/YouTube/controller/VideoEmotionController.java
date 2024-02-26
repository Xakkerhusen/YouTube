package com.example.YouTube.controller;

import com.example.YouTube.dto.VideoEmotionDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.VideoEmotionService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/video_emotion")
public class VideoEmotionController {

    @Autowired
    private VideoEmotionService videoEmotionService;


    @PostMapping("/like")
    @Operation(summary = "API for video like", description = "this api is used to create video like")
    public ResponseEntity<VideoEmotionDTO> createLike(@Valid @RequestBody VideoEmotionDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                      AppLanguage language) {
        log.info("create video like {} ", dto.getVideoId());
        return ResponseEntity.ok(videoEmotionService.createLike(dto, language));
    }

    @PostMapping("/dislike")
    @Operation(summary = "API for create dislike", description = "this api is used to create article dislike")
    public ResponseEntity<VideoEmotionDTO> createDislike(@Valid @RequestBody VideoEmotionDTO dto,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                         AppLanguage language) {
        log.info("create article dislike {} ", dto.getVideoId());
        return ResponseEntity.ok(videoEmotionService.createDislike(dto, language));
    }

    @DeleteMapping("/remove/{pId}/{vId}")
    @Operation(summary = "API for remove like and dislike", description = "this api is used to remove like and dislike")
    public ResponseEntity<Boolean> remove(@PathVariable("pId") Integer pId,
                                          @PathVariable("vId") String vId,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                          AppLanguage language) {
        log.info("remove video like or dislike {} ", vId);
        return ResponseEntity.ok(videoEmotionService.remove(pId, vId, language));
    }


    @GetMapping("/userLikedVideoList")
    @Operation(summary = "API for user liked video list", description = "this api is used to user liked video list")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<VideoEmotionDTO>> getUserLikedList(@RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                                  AppLanguage language) {
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        log.info("get liked video list {} ", profileId);
        return ResponseEntity.ok(videoEmotionService.getUserLikedList(profileId, language));
    }

    @GetMapping("/userLikedVideoListByAdmin/{pId}")
    @Operation(summary = "API for user liked video list by admin", description = "this api is used to user liked video list by admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VideoEmotionDTO>> getUserLikedListByAmin(@PathVariable("pId") Integer pId,
                                                                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                                        AppLanguage language) {
        Integer adminId = SpringSecurityUtil.getCurrentUser().getId();
        log.info("remove video like or dislike {} ", pId);
        return ResponseEntity.ok(videoEmotionService.getUserLikedListByAmin(adminId,pId, language));
    }



}
