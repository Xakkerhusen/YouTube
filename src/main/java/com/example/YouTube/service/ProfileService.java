package com.example.YouTube.service;

import com.example.YouTube.dto.CreatedProfileDTO;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileStatus;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ProfileRepository;
import com.example.YouTube.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    /**
     * In the body of this method, the incoming passwor is checked against the specified conditions by regex.
     * If the password check is successful, the profile will be saved, otherwise an exception will be thrown
     **/
    public CreatedProfileDTO create(CreatedProfileDTO dto, AppLanguage language) {
        if (!dto.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,}$")) {
            log.warn("Profile password required{}", dto.getPassword());
            throw new AppBadException(resourceBundleService.getMessage("password.required", language));
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return dto;
    }

    /**In the body of this method, the incoming password is checked against the conditions specified by regex.
     If the password verification is successful, the profile password will be replaced with the entered password*/
    public boolean changePassword(Integer profileId, String password, AppLanguage language) {
        if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,}$")) {
            log.warn("Profile password required{}", password);
            throw new AppBadException(resourceBundleService.getMessage("password.required", language));
        }

        get(profileId, language);
        Integer effectiveRows = profileRepository.updatePassword(profileId,MD5Util.encode( password));
        return effectiveRows != 0;
    }

    /**this method looks up the input ID from the database.
     * If found, it returns the found object, otherwise it throws an exception*/
    private ProfileEntity get(Integer profileId, AppLanguage language) {
        return profileRepository.findById(profileId).orElseThrow(() -> {
            log.warn("Profile not found{}", profileId);
            return new AppBadException(resourceBundleService.getMessage("profile.not.found", language));
        });

    }
}
