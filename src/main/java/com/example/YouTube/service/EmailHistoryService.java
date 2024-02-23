package com.example.YouTube.service;


import com.example.YouTube.dto.EmailHistoryDTO;
import com.example.YouTube.dto.FilterEmailHistoryDTO;
import com.example.YouTube.dto.PaginationResultDTO;
import com.example.YouTube.dto.RegistrationDTO;
import com.example.YouTube.entity.EmailSentHistoryEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.repository.CustomRepository;
import com.example.YouTube.repository.EmailSentHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class EmailHistoryService {
    @Autowired
    private EmailSentHistoryRepository emailSentHistoryRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private CustomRepository customRepository;

    /**
     * this method is needed to write a link to the history when an sms is sent to an email 👇🏻
     */

    public void create(RegistrationDTO dto, String text) {
        EmailSentHistoryEntity entity = new EmailSentHistoryEntity();
        entity.setEmail(dto.getEmail());
        entity.setMessage(text);
        emailSentHistoryRepository.save(entity);
    }

    /**
     * this method is needed to write to the history when SMS is
     * sent to the newly entered email when the user changes the email address. 👇🏻
     */
    public void create(String email, String text) {
        EmailSentHistoryEntity entity = new EmailSentHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(text);
        emailSentHistoryRepository.save(entity);
    }

    /**
     * this method is needed to get all sms history. 👇🏻
     */
    public PageImpl<EmailHistoryDTO> getAllEmailHistory(Pageable pageable, AppLanguage language) {
        Page<EmailSentHistoryEntity> all = emailSentHistoryRepository.findAll(pageable);
        if (all.isEmpty()) {
            throw new ArithmeticException(resourceBundleService.getMessage("history.empty", language));
        }
        List<EmailSentHistoryEntity> content = all.getContent();
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailSentHistoryEntity entity : content) {
            dtoList.add(toDo(entity));
        }
        return new PageImpl<>(dtoList, pageable, all.getTotalElements());
    }

    private EmailHistoryDTO toDo(EmailSentHistoryEntity entity) {
        EmailHistoryDTO dto = new EmailHistoryDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    /**
     * this method is needed to get all sms history by entered email. 👇🏻
     */
    public PageImpl<EmailHistoryDTO> getAllEmailHistoryByEmail(String email, Pageable pageable, AppLanguage language) {
        Page<EmailSentHistoryEntity> byEmail = emailSentHistoryRepository.findByEmail(email, pageable);
        if (byEmail.isEmpty()) {
            throw new ArithmeticException(resourceBundleService.getMessage("no.history.found.for.this.email", language));
        }
        List<EmailSentHistoryEntity> content = byEmail.getContent();
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailSentHistoryEntity entity : content) {
            dtoList.add(toDo(entity));
        }
        return new PageImpl<>(dtoList, pageable, byEmail.getTotalElements());
    }

    /**
     * this method is needed to get all sms history in a filtered state. 👇🏻
     */
    public PageImpl<EmailHistoryDTO> filter(FilterEmailHistoryDTO dto, Pageable pageable, AppLanguage language) {
        PaginationResultDTO<EmailSentHistoryEntity> resuletFilter = customRepository.emailHistoryFilter(dto, pageable);
        if (resuletFilter.getList().isEmpty()) {
            throw new ArithmeticException(resourceBundleService.getMessage("email.history.not.found", language));
        }
        List<EmailHistoryDTO> dtoList = new LinkedList<>();
        for (EmailSentHistoryEntity entity : resuletFilter.getList()) {
            dtoList.add(toDo(entity));
        }
        return new PageImpl<>(dtoList, pageable, resuletFilter.getTotalSize());
    }
}
