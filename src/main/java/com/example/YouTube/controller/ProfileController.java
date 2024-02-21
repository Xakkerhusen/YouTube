package com.example.YouTube.controller;

import com.example.YouTube.config.CustomUserDetails;
import com.example.YouTube.dto.CreatedProfileDTO;
import com.example.YouTube.dto.ProfileDTO;
import com.example.YouTube.dto.UpdateProfileDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.ProfileService;
import com.example.YouTube.utils.SpringSecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * This class is created for the methods of the profile
 */
@Slf4j
@Tag(name = "Profile API list", description = "API list for Profile")
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    /**
     * this method is used to create profiles on the admin side
     */
    @PostMapping("/adm")
    @Operation(summary = "Api for create", description = "this api is used to create profile ")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody CreatedProfileDTO dto,
                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("Create profile {}", dto.getEmail());
        return ResponseEntity.ok(profileService.create(dto, language));
    }

    /**
     * this method is used to change each profile's own password
     */
    @PostMapping("/any/{password}")
    @Operation(summary = "Api for changePassword", description = "this api is used to change password ")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    public ResponseEntity<?> changePassword(@PathVariable String password,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("change password profile ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(profileService.changePassword(profileId, password, language));
    }

    @PostMapping("/any")
    @Operation(summary = "Api for updateEmail", description = "this api is used to updated email ")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    public ResponseEntity<?> updateEmail(@RequestBody(required = false) UpdateProfileDTO dto,
                                         @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("update  email ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        String name = SpringSecurityUtil.getCurrentUser().getName();
        return ResponseEntity.ok(profileService.updateEmail(profileId,name,dto,language));
    }
    @GetMapping("/verification/email/{jwt}")
    @Operation( summary = "Api for verification by email", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO> emailVerification(@PathVariable("jwt") String jwt,
                                                        @RequestHeader(value = "Accept-Language",defaultValue = "UZ") AppLanguage language) {
        log.info("verification {}", jwt);
        return ResponseEntity.ok(profileService.emailVerification(jwt,language));
    }

    @PostMapping("/any/detail")
    @Operation(summary = "Api for updateProfileDetail", description = "this api is used to updated detail ")
    @PreAuthorize("hasAnyRole('ADMIN','USER','MODERATOR')")
    public ResponseEntity<?> updateProfileDetail(@RequestBody(required = false) UpdateProfileDTO dto,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        log.info("change password profile ");
        Integer profileId = SpringSecurityUtil.getCurrentUser().getId();
        return ResponseEntity.ok(profileService.updateProfileDetail(profileId, dto, language));
    }



}
