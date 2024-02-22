package com.example.YouTube.controller;

import com.example.YouTube.dto.VideoCreateDTO;
import com.example.YouTube.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Video API list", description = "API list for Video")
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/create")
    @Operation(summary = "Api for create", description = "this api is used to create video")
    public ResponseEntity<VideoCreateDTO> create(@Valid @RequestBody VideoCreateDTO dto) {
        return ResponseEntity.ok(videoService.create(dto));
    }


}
