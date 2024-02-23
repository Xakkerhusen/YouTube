package com.example.YouTube.service;

import com.example.YouTube.dto.CommentDTO;
import com.example.YouTube.dto.CommentInfoDTO;
import com.example.YouTube.dto.CommentPaginationDTO;
import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.entity.CommentEntity;
import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.AttachRepository;
import com.example.YouTube.repository.CommetRepository;
import com.example.YouTube.repository.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CommentService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AttachRepository attachRepository;

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

    public CommentEntity get(String id, AppLanguage language) {
        return repository.findByReplyId(id).orElseThrow(() ->
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
     * This method shows all the comments for Admin.
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

    public CommentPaginationDTO dtoComment(CommentEntity entity) {
        CommentPaginationDTO dto = new CommentPaginationDTO();
        dto.setCommentID(entity.getId());
        dto.setTitle(entity.getVideo().getTitle());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        String attachId = entity.getVideo().getAttachId();
        Optional<AttachEntity> optional = attachRepository.findAttachID(attachId);
        if (optional.isEmpty()) {
            dto.setDuration(null);
        } else {
            AttachEntity attachEntity = optional.get();
            dto.setDuration(attachEntity.getDuration());
        }
        dto.setVideoName(entity.getVideo().getTitle());
        dto.setContent(entity.getContent());
        dto.setPreviewAttachID(entity.getVideo().getPreviewAttachId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoID(entity.getVideoId());
        return dto;
    }


    /**
     * Bu metod orqali ADMIN berilgan userga tegishli barcha
     * commentlarni to`liq ko`rishi mumkin
     */

    public PageImpl<CommentPaginationDTO> paginationProfile(Integer profileID, Integer size, Integer page, AppLanguage lan) {

        Optional<ProfileEntity> byId = profileRepository.findById(profileID);
        if (byId.isEmpty()) {
            log.warn("comment not fount");
            String message = service.getMessage("comment.not.fount", lan);
            throw new AppBadException(message);
        }

        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<CommentEntity> commentEntities = repository.profileID(profileID, pageable);
        List<CommentEntity> entityList = commentEntities.getContent();
        if (entityList.isEmpty()) {
            throw new AppBadException("bu user hali izoh yozmagan.");
        }
        long totalElements = commentEntities.getTotalElements();
        List<CommentPaginationDTO> dtoList = new ArrayList<>();

        for (CommentEntity comment : entityList) {
            dtoList.add(dtoComment(comment));
        }
        return new PageImpl<>(dtoList, pageable, totalElements);

    }

    public List<CommentInfoDTO> getListVideo(String videoId, AppLanguage lan) {
        List<CommentEntity> list = repository.videoID(videoId);
        if (list.isEmpty()) {
            String message = service.getMessage("video.no.comment", lan);
            log.warn(message);
            throw new AppBadException(message);
        }
        List<CommentInfoDTO> commentList = new LinkedList<>();
        for (CommentEntity entity : list) {
            commentList.add(videoDTO(entity));
        }
        return commentList;
    }

    private CommentInfoDTO videoDTO(CommentEntity entity) {
        CommentInfoDTO dto = new CommentInfoDTO();
        dto.setCommentID(entity.getId());
        dto.setLikeCount(entity.getLikeCount());
        dto.setDislikeCount(entity.getDislikeCount());
        dto.setProfileID(entity.getProfileId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setContent(entity.getContent());
        dto.setProfileName(entity.getProfile().getName());
        dto.setProfileSurname(entity.getProfile().getSurname());
        dto.setProfilePhotoId(entity.getProfile().getAttachId());
        return dto;
    }

    public List<CommentInfoDTO> getreplyIdList(String commentID, AppLanguage language) {
        if (repository.commentID(commentID).isEmpty()) {
            String message = service.getMessage("comment.empty", language);
            log.warn(message);
            throw new AppBadException(message);
        }
        List<CommentEntity> byReplyIdComment = repository.findByReplyIdComment(commentID);
        List<CommentInfoDTO> list = new LinkedList<>();
        for (CommentEntity entity : byReplyIdComment) {
            list.add(videoDTO(entity));
        }
        return list;

    }
}
