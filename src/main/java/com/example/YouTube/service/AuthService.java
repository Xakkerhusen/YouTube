package com.example.YouTube.service;


import com.example.YouTube.dto.Auth;
import com.example.YouTube.dto.ProfileDTO;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileStatus;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ProfileRepository;
import com.example.YouTube.utils.JWTUtil;
import com.example.YouTube.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    /**this method is used for profile login. In this, the necessary information is requested from the profile, and the profile database with this information is checked, and its status is checked.
     If the checks are successful, the JWT is provided to the profile, otherwise an exception is thrown*/

    public ProfileDTO login(Auth auth, AppLanguage language) {
        Optional<ProfileEntity> optional =
                profileRepository.findByEmailAndPassword(auth.getEmail(), MD5Util.encode(auth.getPassword()));
        if (optional.isEmpty()||!optional.get().getVisible()){
            log.warn("email.password.wrong {}",auth.getEmail());
            throw new AppBadException(resourceBundleService.getMessage("email.password.wrong",language));
        }
        if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            log.warn("Profile not active {}",optional.get().getStatus());
            throw new AppBadException(resourceBundleService.getMessage("profile.not.active",language));
        }
        ProfileEntity entity = optional.get();
        ProfileDTO dto = new ProfileDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setRole(entity.getRole());
//        dto.setJwt(JWTUtil.encode(entity.getId(), entity.getRole()));

        dto.setJwt(JWTUtil.encodeForSpringSecurity(entity.getEmail(), entity.getRole()));

        return dto;
    }


}
