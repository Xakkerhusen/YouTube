package com.example.YouTube.controller;

import com.example.YouTube.dto.*;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Channel API list", description = "API list for Channel")
@RestController
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @Operation(summary = "Api for creat", description = "this api used for creat channel")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/creat")
    public String creat(@Valid @RequestBody CreatChannelDTO channelDTO,
                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return channelService.creat(channelDTO, language);
    }

    @Operation(summary = "Api for update", description = "this api used for update the value of (name,description) variables")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update/{channel_id}")
    public ResponseEntity<ChannelDTO> update(@PathVariable("channel_id") String channelId,
                                             @Valid @RequestBody UpdateChannelDTO channelDTO,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(channelService.update(channelId, channelDTO, language));
    }

    @Operation(summary = "Api for update", description = "this api used for update the Channel Photo")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updatePhoto/{channel_id}")
    public String updatePhoto(@PathVariable("channel_id") String channelId,
                              @Valid @RequestBody ChannelImageDTO imageDTO,
                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return channelService.updatePhoto(channelId, imageDTO.getImageId(), language);
    }

    @Operation(summary = "Api for update", description = "this api used for update the Channel Banner")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/updateBanner/{channel_id}")
    public String updateBanner(@PathVariable("channel_id") String channelId,
                               @Valid @RequestBody ChannelImageDTO imageDTO,
                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return channelService.updateBanner(channelId, imageDTO.getImageId(), language);
    }

    @Operation(summary = "Api for get", description = "this api used for get channels by Pagination")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByPagination")
    public ResponseEntity<PageImpl<ChannelDTO>> getByPagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(channelService.getByPagination(page, size, language));
    }

    @Operation(summary = "Api for get", description = "this api used for get channel by id")
    @GetMapping("/getById/{channel_id}")
    public ResponseEntity<ChannelDTO> getById(@PathVariable("channel_id") String channelId,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(channelService.getById(channelId, language));
    }

    @Operation(summary = "Api for update", description = "this api used for update the Channel Status")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    @PutMapping("/updateStatus/{channel_id}")
    public String updateStatus(@PathVariable("channel_id") String channelId,
                               @RequestBody ChannelStatusDTO status,
                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return channelService.updateStatus(channelId, status.getStatus(), language);
    }

    @Operation(summary = "Api for get", description = "this api used for get channel list")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    @GetMapping("/getChannelList")
    public ResponseEntity<List<ChannelDTO>> getChannelList(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(channelService.getChannelList(language));
    }


}
