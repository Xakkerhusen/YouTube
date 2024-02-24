package com.example.YouTube.controller;

import com.example.YouTube.config.CustomUserDetails;
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
        CustomUserDetails currentUser = SpringSecurityUtil.getCurrentUser();
        return ResponseEntity.ok(playlistService.create(currentUser.getId(),dto, language));
    }

    @PutMapping("/update/{playlist_id}")
    @Operation(summary = "Api for playlist", description = "this api is used to updated playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update( @PathVariable("playlist_id") Integer playlistId,
                                             @Valid @RequestBody(required = false) PlaylistDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update  playlist ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistService.update(playlistId,profileId,dto,language));
    }

    @PutMapping("/updateStatus/{playlist_id}")
    @Operation(summary = "Api for playlist", description = "this api is used to updated status playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateStatus( @PathVariable("playlist_id") Integer playlistId,
                                     @Valid @RequestBody(required = false) PlaylistDTO dto,
                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update status  playlist ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistService.update(playlistId,profileId,dto,language));
    }

    @DeleteMapping("/delete/{playlist_id}")
    @Operation(summary = "Api for playlist", description = "this api is used to delete playlist ")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> delete( @PathVariable("playlist_id") Integer playlistId,
                                           @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update status  playlist ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistService.delete(playlistId,profileId,language));
    }

    @GetMapping("/pagination")
    @Operation(summary = "Api for playlist", description = "this api is used to pagination playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> pagination(@RequestParam Integer page, Integer size,
                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update status  playlist ");
        return ResponseEntity.ok(playlistService.pagination(page,size,language));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Api for playlist", description = "this api is used to delete playlist ")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update status  playlist ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(playlistService.getPlaylistByUser(profileId,language));
    }


}
