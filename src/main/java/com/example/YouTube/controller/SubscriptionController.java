package com.example.YouTube.controller;

import com.example.YouTube.config.CustomUserDetails;
import com.example.YouTube.dto.SubscriptionCreateDTO;
import com.example.YouTube.dto.SubscriptionInfoDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.SubscriptionService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 1. Create User Subscription (USER)
//        channel_id,notification_type (keladigna parametr)
//    2. Change Subscription Status (USER)
//        channel_id,status
//    3. Change Subscription Notification type (USER)
//        channel_id,notification_type
//    4. Get User Subscription List (only Active) (murojat qilgan user)
//        SubscriptionInfo
//    5. Get User Subscription List By UserId (only Active) (ADMIN)
//        SubscriptionInfo + created_date
//
//        SubscriptionInfo
//            id,channel(id,name,photo(id,url)),notification_type
@Slf4j
@Tag(name = "Subscription API list", description = "API list for Subscription")
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    @Operation(summary = "Api for create", description = "this api used for create Subscription")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public String create(@RequestBody SubscriptionCreateDTO dto,
                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        Integer profileId= SpringSecurityUtil.getCurrentUser().getId();
        return subscriptionService.create(profileId,dto,language);
    }

    @Operation(summary = "Api for update", description = "this api used for change Subscription status")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/changeSubscriptionStatus")
    public String changeSubscriptionStatus(@RequestBody ChangeSubscriptionStatusDTO dto,
                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        Integer profileId= SpringSecurityUtil.getCurrentUser().getId();
        return subscriptionService.changeSubscriptionStatus(profileId,dto,language);
    }

    @Operation(summary = "Api for update", description = "this api used for change Notification type")
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/changeNotificationType")
    public String changeNotificationType(@RequestBody ChangeSubscriptionNotificationDTO dto,
                                           @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        Integer profileId= SpringSecurityUtil.getCurrentUser().getId();
        return subscriptionService.changeNotificationType(profileId,dto,language);
    }

    @Operation(summary = "Api for get", description = "this api used for get Info")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getInfo")
    public ResponseEntity<List<SubscriptionInfoDTO>> getInfo(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        Integer profileId= SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(subscriptionService.getInfo(profileId,language));
    }

    @Operation(summary = "Api for get", description = "this api used for get Info By Admin")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getInfoByAdmin")
    public ResponseEntity<List<SubscriptionInfoDTO>> getInfoByAdmin(@RequestParam("profile_id")Integer profileId,
                                                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language){
        return ResponseEntity.ok(subscriptionService.getInfoByAdmin(profileId,language));
    }

}
