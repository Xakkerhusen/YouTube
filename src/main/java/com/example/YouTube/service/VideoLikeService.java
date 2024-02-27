package com.example.YouTube.service;


import com.example.YouTube.dto.CreatedLikeDTO;
import com.example.YouTube.entity.CommentLikeEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.repository.VideoLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class VideoLikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public Object create(String commentId, Integer profileId, CreatedLikeDTO dto, AppLanguage language) {
        commentService.get(commentId, language);
        Optional<CommentLikeEntity> optional = videoLikeRepository.findTop1ByCommentId(commentId, profileId);

        CommentLikeEntity commentEntity = new CommentLikeEntity();

        if (optional.isEmpty()) {
            commentEntity.setCommentId(commentId);
            commentEntity.setProfileId(profileId);
            commentEntity.setType(String.valueOf(dto.getStatus()));
            videoLikeRepository.save(commentEntity);
            return "SUCCESS save";
        }
        CommentLikeEntity entity = optional.get();
        if (entity.getType().equals(dto.getStatus().toString())) {
            videoLikeRepository.deleteById(entity.getId());
            return "SUCCESS delete";
        } else if (entity.getCommentId().equals(commentId) && !entity.getProfileId().equals(profileId)) {
            commentEntity.setCommentId(commentId);
            commentEntity.setProfileId(profileId);
            commentEntity.setType(String.valueOf(dto.getStatus()));
            videoLikeRepository.save(commentEntity);
            return "SUCCESS save";
        } else {
            return videoLikeRepository.update(entity.getId(), String.valueOf(dto.getStatus()), LocalDateTime.now()) != 0 ? "update" : "articleId not found";
        }

    }


//    public List<CommentInfoDTO> userLikedCommentList(Integer profileId, AppLanguage language) {
//        List<CommentLikeEntity> list = commentLikeRepository.getByProfileId(profileId);
//        if (list.isEmpty()) {
//            throw new ArithmeticException(resourceBundleService.getMessage("this.profile.has.not.been.commented", language));
//        }
//        List<CommentInfoDTO> dtoList = new LinkedList<>();
//        for (CommentLikeEntity entity : list) {
//            dtoList.add(commentService.videoDTO(entity.getComment()));
//        }
//        return dtoList;
//    }
//
//    public List<CommentLikeInfoDTO> userLikedCommentListByUserId(Integer id, AppLanguage language) {
//        List<CommentLikeEntity> entities = commentLikeRepository.findByProfileId(id);
//        if (entities.isEmpty()) {
//            throw new ArithmeticException(resourceBundleService.getMessage("this.profile.has.not.been.commented", language));
//        }
//        List<CommentLikeInfoDTO> dtoList = new LinkedList<>();
//        for (CommentLikeEntity entity : entities) {
//            dtoList.add(toDo(entity));
//        }
//        return dtoList;
//    }
//
//    private CommentLikeInfoDTO toDo(CommentLikeEntity entity) {
//        CommentLikeInfoDTO dto = new CommentLikeInfoDTO();
//        dto.setId(entity.getId());
//        dto.setCommentId(entity.getCommentId());
//        dto.setProfileId(entity.getProfileId());
//        dto.setType(entity.getType());
//        dto.setCreatedDate(entity.getCreatedDate());
//        return dto;
//    }
}
