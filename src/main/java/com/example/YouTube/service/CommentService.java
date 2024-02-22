package com.example.YouTube.service;

import com.example.YouTube.dto.CommentDTO;
import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.entity.CommentEntity;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.CommetRepository;
import com.example.YouTube.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResourceBundleService service;

    @Autowired
    private CommetRepository repository;

    /**
     * It ' a method in which the
     * USER can record a comment on any video.
     */
    public CreateCommentDTO create(CreateCommentDTO dto, Integer profileID, AppLanguage language) {
        CommentEntity entity = new CommentEntity();
        if (dto.getReplyID() != null) {
            get(dto.getReplyID(), language);
            entity.setReplyId(dto.getReplyID());
        }
        entity.setCreatedDate(LocalDateTime.now());
        entity.setProfileId(profileID);
        entity.setContent(dto.getContent());
        entity.setVideoId(dto.getVideoID());
        repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }


    /**
     * In this method, it is checked that
     * the user can write an answer to
     * any comment, that is, a comment.
     */

    public CommentEntity get(Integer id, AppLanguage language) {
        return repository.findById(id).orElseThrow(() ->
        {
            String message = service.getMessage("replyID.wrong", language);
            log.warn(message);
            throw new AppBadException(message);
        });
    }

    public CommentEntity getCommitId(String id, AppLanguage language) {
        return repository.commentID(id).orElseThrow(() ->
        {
            String message = service.getMessage("commentID.wrong", language);
            log.warn(message);
            throw new AppBadException(message);
        });
    }

    /**
     * This can change the annotation the user wrote in the method
     */
    public String update(CreateCommentDTO dto, Integer profileID, AppLanguage language, String commentId) {
        CommentEntity entity = getCommitId(commentId, language);
        if (Objects.equals(entity.getProfileId(), profileID)) {
            entity.setContent(dto.getContent());
            entity.setUpdateDate(LocalDateTime.now());
            entity.setReplyId(dto.getReplyID());
            repository.save(entity);
            return service.getMessage("update.successful", language);
        }
        String message = service.getMessage("comment.no.update", language);
        log.warn(message);
        throw new AppBadException(message);
    }


    /**
     * this will delete the user's
     * comments written through the method.
     * This work is done by the user himself or ADMIN.
     */
    public Boolean delete(Integer profileID, AppLanguage language, String commentId) {
        CommentEntity commitId = getCommitId(commentId, language);
        Optional<ProfileEntity> byId = profileRepository.findById(profileID);
        if (byId.isEmpty()) {
            String message = service.getMessage("profileID.not.found", language);
            log.warn(message);
            throw new AppBadException(message);
        }
        if (Objects.equals(commitId.getProfileId(), profileID) || byId.get().getRole().equals(ProfileRole.ROLE_ADMIN)) {
            repository.delete(commitId);
            return true;
        }
        String message = service.getMessage("profileID.not.found", language);
        log.warn(message);
        throw new AppBadException(message);

    }


    /**
     *This method shows all the comments for Admin.
     */

    public PageImpl<CommentDTO> pagination(Integer size, Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> studentPage = repository.findAll(pageable);
        List<CommentEntity> entityList = studentPage.getContent();
        long totalElements = studentPage.getTotalElements();
        List<CommentDTO> dtoList = new ArrayList<>();

        for (CommentEntity comment : entityList) {
            dtoList.add(dto(comment));
        }
        return new PageImpl<>(dtoList, pageable, totalElements);
    }

    public CommentDTO dto(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setContent(entity.getContent());
        dto.setProfileId(entity.getProfileId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoId(entity.getVideoId());
        dto.setUpdateDate(entity.getUpdateDate());
        return dto;
    }
}
