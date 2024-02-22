package com.example.YouTube.service;

import com.example.YouTube.dto.CreateCommentDTO;
import com.example.YouTube.entity.CategoryEntity;
import com.example.YouTube.entity.CommentEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.CommetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    @Autowired
    private ResourceBundleService service;

    @Autowired
    private CommetRepository repository;

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

    public CommentEntity get(Integer replyId, AppLanguage language) {
        return repository.findById(replyId).orElseThrow(() ->
        {
            String message = service.getMessage("replyID.wrong", language);
            throw new AppBadException(message);
        });
    }


}
