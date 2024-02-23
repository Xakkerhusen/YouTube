package com.example.YouTube.controller;

import com.example.YouTube.dto.VideoCreateDTO;
import com.example.YouTube.dto.VideoDTO;
import com.example.YouTube.dto.VideoStatusDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Video API list", description = "API list for Video")
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @PostMapping("/create")
    @Operation(summary = "Api for create", description = "this api is used to create video")
    public ResponseEntity<VideoCreateDTO> create(@Valid @RequestBody VideoCreateDTO dto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                 AppLanguage language) {
        log.info("Video create ");
        return ResponseEntity.ok(videoService.create(dto, language));
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Api for update", description = "this api is used to update video")
    public ResponseEntity<Boolean> update(@Valid @PathVariable("id") String id, @RequestBody VideoCreateDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                          AppLanguage language) {
        log.info("Video update detail{} ", id);
        return ResponseEntity.ok(videoService.update(id, dto, language));
    }


    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "Api for update status", description = "this api is used to update video status")
    public ResponseEntity<Boolean> updateStatus(@Valid @PathVariable("id") String id, @RequestBody VideoStatusDTO dto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                AppLanguage language) {
        log.info("Video update status{} ", id);
        return ResponseEntity.ok(videoService.updateStatus(id, dto, language));
    }


    @PutMapping("/increaseViewCountByVideoId/{id}")
    @Operation(summary = "API for increase view count", description = "this api is used to increase video view count")
    public ResponseEntity<Boolean> increaseViewCount(@PathVariable("id") String videoId,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                     AppLanguage language) {
        log.info("Increase view count{}", videoId);
        return ResponseEntity.ok(videoService.increaseViewCount(videoId, language));
    }


    @GetMapping("/paginationCategoryId")
    @Operation(summary = "API for pagination", description = "this api is used to pagination by categoryId")
    public ResponseEntity<PageImpl<VideoDTO>> paginationCategory(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                                 @RequestParam(value = "category", defaultValue = "2") Integer categoryId,
                                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                                 AppLanguage language) {
        log.info("Video pagination{}", categoryId);
        return ResponseEntity.ok(videoService.paginationCategoryId(page, size, categoryId, language));
    }


    @GetMapping("/search")
    @Operation(summary = "Api for search by title", description = "this api is used to search by title")
    public ResponseEntity<List<VideoDTO>> search(@RequestParam("title") String title,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                 AppLanguage language) {
        log.info("Video search by title{} ", title);
        return ResponseEntity.ok(videoService.search(title, language));
    }


    @GetMapping("/getById/{id}")
    @Operation(summary = "Api for get by id", description = "this api is used to get by id")
    public ResponseEntity<VideoDTO> getById(@PathVariable("id") String id,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                            AppLanguage language) {
        log.info("Video get by id{} ", id);
        return ResponseEntity.ok(videoService.getById(id, language));
    }


    @GetMapping("/videoList")
    @Operation(summary = "Api for video list pagination", description = "this api is used to video list pagination")
    public ResponseEntity<PageImpl<VideoDTO>> paginationVideoList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                                  AppLanguage language) {
        log.info("Video pagination ");
        return ResponseEntity.ok(videoService.paginationVideoList(page, size, language));
    }


    @GetMapping("/channelVideoList/{id}")
    @Operation(summary = "Api for channel video list pagination", description = "this api is used to channel video list pagination")
    public ResponseEntity<PageImpl<VideoDTO>> ChannelVideoListPagination(@PathVariable("id") String id,
                                                                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "size", defaultValue = "2") Integer size,
                                                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ")
                                                                         AppLanguage language) {
        log.info("Channel video list pagination ");
        return ResponseEntity.ok(videoService.paginationChannelVideoList(page, size, language, id));
    }


}
