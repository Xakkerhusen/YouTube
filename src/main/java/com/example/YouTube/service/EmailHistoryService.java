package com.example.YouTube.service;


import com.example.YouTube.dto.RegistrationDTO;
import com.example.YouTube.entity.EmailSentHistoryEntity;
import com.example.YouTube.repository.EmailSentHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailHistoryService {
    @Autowired
    private EmailSentHistoryRepository emailSentHistoryRepository;

    public void create(RegistrationDTO dto, String text) {
        EmailSentHistoryEntity entity = new EmailSentHistoryEntity();
        entity.setEmail(dto.getEmail());
        entity.setMessage(text);
        emailSentHistoryRepository.save(entity);
    }
    public void create(String email, String text) {
        EmailSentHistoryEntity entity = new EmailSentHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(text);
        emailSentHistoryRepository.save(entity);
    }
}
