package com.example.YouTube.service;

import com.example.YouTube.dto.TagNameDTO;
import com.example.YouTube.entity.TagNameEntity;
import com.example.YouTube.repository.TagNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TagNameService {

    @Autowired
    private TagNameRepository tagNameRepository;

    public TagNameDTO create(TagNameDTO dto) {

        TagNameEntity entity = new TagNameEntity();

        entity.setTagName(dto.getTagName());
        entity.setCreatedDate(LocalDateTime.now());
        tagNameRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setTagName(entity.getTagName());

        return dto;
    }


    public Boolean update(Integer id, TagNameDTO dto) {
        TagNameEntity entity = get(id);
        entity.setTagName(dto.getTagName());
        tagNameRepository.save(entity);
        return true;
    }

    public Boolean delete(Integer id) {
        TagNameEntity entity = get(id);
        tagNameRepository.delete(entity);
        return true;
    }


    public List<TagNameDTO> getAll() {
        Iterable<TagNameEntity> all = tagNameRepository.findAll();
        List<TagNameDTO> dtoList = new LinkedList<>();
        for (TagNameEntity entity : all) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }

    public TagNameDTO toDTO(TagNameEntity entity) {
        TagNameDTO dto = new TagNameDTO();
        dto.setId(entity.getId());
        dto.setTagName(entity.getTagName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public TagNameEntity get(Integer id) {
        Optional<TagNameEntity> optional = tagNameRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Tag name not found {}", id);
            throw new ArithmeticException("Tag name not found");
        }
        return optional.get();
    }


}
