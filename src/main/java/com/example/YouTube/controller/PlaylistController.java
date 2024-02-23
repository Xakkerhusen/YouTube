package com.example.YouTube.controller;

import com.example.YouTube.dto.CreatePlaylistDTO;
import com.example.YouTube.dto.PlaylistDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.PlaylistService;
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
@Tag(name = "Playlist API list", description = "API list for Playlist")
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @PostMapping("")
    @Operation(summary = "Api for create", description = "this api is used to create playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody CreatePlaylistDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Create playlist {}", dto.getName());
        return ResponseEntity.ok(playlistService.create(dto, language));
    }

    @PutMapping("/update/{playlist_id}")
    @Operation(summary = "Api for playlist", description = "this api is used to updated playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updatePlaylist( @PathVariable("playlist_id") Long playlistId,
                                             @Valid @RequestBody(required = false) PlaylistDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update  playlist ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistService.update(playlistId,profileId,dto,language));
    }
}
