package com.example.YouTube.service;

import com.example.YouTube.dto.*;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileStatus;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ProfileRepository;
import com.example.YouTube.utils.JWTUtil;
import com.example.YouTube.utils.MD5Util;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private AttachService attachService;

    /**
     * In the body of this method, the incoming passwor is checked against the specified conditions by regex.
     * If the password check is successful, the profile will be saved, otherwise an exception will be thrown 👇🏻
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

    /**
     * In the body of this method, the incoming password is checked against the conditions specified by regex.
     * If the password verification is successful,
     * the profile password will be replaced with the entered password 👇🏻
     */
    public boolean changePassword(Integer profileId, String password, AppLanguage language) {
        if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{5,}$")) {
            log.warn("Profile password required{}", password);
            throw new AppBadException(resourceBundleService.getMessage("password.required", language));
        }

        get(profileId, language);
        Integer effectiveRows = profileRepository.updatePassword(profileId, MD5Util.encode(password));
        return effectiveRows != 0;
    }

    /**
     * This method is used to update the profile's email address 👇🏻
     */
    public Boolean updateEmail(Integer profileId, String name, UpdateProfileDTO dto, AppLanguage language) {
        ProfileEntity entity = get(profileId, language);
        String jwt = JWTUtil.encodeForSpringSecurity2(dto.getEmail(), entity.getRole(), profileId);
        String text = "<h1 style=\"text-align: center\">Hello %s</h1>\n" +
                "<p style=\"background-color: indianred; color: white; padding: 30px\">Follow the link below to complete your email change</p>\n" +
                "<a style=\" background-color: #f44336;\n" +
                "  color: white;\n" +
                "  padding: 14px 25px;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  display: inline-block;\" href=\"http://localhost:8080/profile/verification/email/%s\n" +
                "\">Click</a>\n" +
                "<br>\n";
        text = String.format(text, name, jwt);
        mailSenderService.sendEmail(dto.getEmail(), "Change email", text);

        emailHistoryService.create(dto.getEmail(), text);
        return true;
    }

    /**
     * this method is used to change the user's email after using the link sent to the user's entered email 👇🏻
     */
    public ProfileDTO emailVerification(String jwt, AppLanguage language) {
        ProfileDTO profile = new ProfileDTO();
        try {
            JWTDTO jwtDTO = JWTUtil.decodeForSpringSecurity2(jwt);

            Optional<ProfileEntity> optional = profileRepository.findById(jwtDTO.getId());
            if (optional.isEmpty()) {
                log.warn("Profile not found {}", jwtDTO.getId());
                throw new AppBadException(resourceBundleService.getMessage("profile.not.found", language));
            }
            ProfileEntity entity = optional.get();
            profileRepository.updateEmail(entity.getId(), jwtDTO.getEmail());
            profile.setName(entity.getName());
            profile.setSurname(entity.getSurname());
            profile.setRole(entity.getRole());

        } catch (JwtException e) {
            log.warn("Please tyre again. {}", jwt);
            throw new AppBadException(resourceBundleService.getMessage("please.tyre.again", language));
        }
        return profile;
    }

    /**
     * this method is used to change user profile details👇🏻
     */
    public Boolean updateProfileDetail(Integer profileId, UpdateProfileDTO dto, AppLanguage language) {
        ProfileEntity entity = get(profileId, language);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        profileRepository.save(entity);
        return true;
    }

    /**
     * this method is used to change profile picture 👇🏻
     */
    public Boolean updateProfileAttach(Integer profileId, MultipartFile file, AppLanguage language) {
        String oldAttach = get(profileId, language).getAttachId();
        Integer effectiveRows = profileRepository.updatePhoto(profileId, attachService.save(file).getId());
        if (oldAttach != null) {
            attachService.delete(oldAttach, language);
        }
        return effectiveRows != 0;
    }

    /**
     * this method is used to get complete information about the user's profile👇🏻
     */
    public ProfileDTO getDetails(Integer profileId, AppLanguage language) {
        ProfileEntity entity = get(profileId, language);
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setAttach(attachService.toDTOForProfile(attachService.get(entity.getAttachId(), language)));
        dto.setEmail(entity.getEmail());
        return dto;
    }

    /**
     * this method looks up the input ID from the database.
     * If found, it returns the found object, otherwise it throws an exception 👇🏻
     */
    private ProfileEntity get(Integer profileId, AppLanguage language) {
        return profileRepository.findById(profileId).orElseThrow(() -> {
            log.warn("Profile not found{}", profileId);
            return new AppBadException(resourceBundleService.getMessage("profile.not.found", language));
        });

    }
}
