package com.example.YouTube.controller;

import com.example.YouTube.dto.VideoTagCreateDTO;
import com.example.YouTube.dto.VideoTagDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.VideoTagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video_tag")
public class VideoTagController {
    @Autowired
    private VideoTagService videoTagService;

    @Operation(summary = "Api for create", description = "this api used for create video tag")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public void add(@Valid  @RequestBody VideoTagCreateDTO tagCreateDTO,
                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        videoTagService.add(tagCreateDTO,language);
    }

    @Operation(summary = "Api for delete", description = "this api used for delete video tag")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete")
    public void delete(@Valid  @RequestBody VideoTagCreateDTO tagCreateDTO,
                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        videoTagService.delete(tagCreateDTO,language);
    }

    @Operation(summary = "Api for get", description = "this api used for get Video Tag List By VideoId")
    @GetMapping("/getVideoTagListByVideoId/{video_id}")
    public ResponseEntity<List<VideoTagDTO>> getVideoTagListByVideoId(@PathVariable("video_id") String videoId,
                                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        return ResponseEntity.ok(videoTagService.getVideoTagListByVideoId(videoId,language));
    }
}
