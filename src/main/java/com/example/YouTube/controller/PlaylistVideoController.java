package com.example.YouTube.controller;


import com.example.YouTube.config.CustomUserDetails;
import com.example.YouTube.dto.PlaylistVideoDTO;
import com.example.YouTube.enums.AppLanguage;

import com.example.YouTube.service.PlaylistVideoService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Playlist_Video API list", description = "API list for Playlist_Video")
@RestController
@RequestMapping("/playlist_video")
public class PlaylistVideoController {


    private final PlaylistVideoService playlistVideoService;

    @Autowired
    public PlaylistVideoController(PlaylistVideoService playlistVideoService) {
        this.playlistVideoService = playlistVideoService;
    }

    @PostMapping("/create")
    @Operation(summary = "Api for create", description = "this api is used to create playlist-video ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody PlaylistVideoDTO dto,
                                    @RequestParam(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Create playlist_video {}");
        CustomUserDetails currentUser = SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok(playlistVideoService.create(currentUser.getId(), dto, language));
    }

    @PutMapping("/update")
    @Operation(summary = "Api for update", description = "this api is used to update playlist-video ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(@Valid @RequestBody PlaylistVideoDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update playlist-video");
        CustomUserDetails currentUser = SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok(playlistVideoService.update(currentUser.getId(), dto, language));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Api for playlist", description = "this api is used to delete playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@RequestBody PlaylistVideoDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("delete status  playlist-video ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistVideoService.delete(profileId, dto, language));
    }

    @GetMapping("/getAllByPlaylistId")
    @Operation(summary = "Api for playlist", description = "this api is used to get list of playlist-video ")
    public ResponseEntity<?> getAllByPlaylistId(@RequestParam Integer playlistId,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("get list of playlist-video ");

        return ResponseEntity.ok(playlistVideoService.getPlaylistVideoInfo(playlistId, language));
    }


}
