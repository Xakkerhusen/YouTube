package com.example.YouTube.controller;

import com.example.YouTube.service.PlaylistVideoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Playlist_Video API list", description = "API list for Playlist_Video")
@RestController
@RequestMapping("/playlist_video")
public class PlaylistVideoController {

    @Autowired
    private PlaylistVideoService playlistVideoService;



}
