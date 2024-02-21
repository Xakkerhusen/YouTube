package com.example.YouTube.service;

import com.example.YouTube.dto.TagNameDTO;
import com.example.YouTube.entity.TagNameEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
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

    @Autowired
    private ResourceBundleService resourceBundleService;



    /**
     * This method checks whether the incoming tag name exists in the database by tag name.
     * if the tag name for this name does not exist in the database,
     * the tag name is saved, otherwise an exception is thrown
     **/
    public TagNameDTO create(TagNameDTO dto,AppLanguage language) {
        Optional<TagNameEntity> optional = tagNameRepository.findByTagName(dto.getTagName());
        if (optional.isPresent()){
            log.warn("Tag name already exist {}", optional.get().getId());
            throw new AppBadException(resourceBundleService.getMessage("tag.name.already.exist",language));
        }

        TagNameEntity entity = new TagNameEntity();
        entity.setTagName(dto.getTagName());
        entity.setCreatedDate(LocalDateTime.now());
        tagNameRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setTagName(entity.getTagName());

        return dto;
    }



    /**
     * This method checks whether the incoming tag name exists in the database.
     * if a tag name of that name does not exist in the database,
     * the tag name will be updated, otherwise an exception will be thrown
     **/
    public Boolean update(Integer id, TagNameDTO dto, AppLanguage language) {
        List<TagNameDTO> all = getAll();
        for (TagNameDTO tag : all) {
            if (tag.getTagName().equals(dto.getTagName())) {
                log.warn("Tag name already exist {}", id);
                throw new AppBadException(resourceBundleService.getMessage("tag.name.already.exist", language));
            }
        }
        TagNameEntity entity = get(id, language);
        entity.setTagName(dto.getTagName());
        tagNameRepository.save(entity);
        return true;
    }



    /**
     * This method searches the database by tag name's id.
     * If found, it deletes the found object, otherwise it throws an exception
     * */
    public Boolean delete(Integer id, AppLanguage language) {
        TagNameEntity entity = get(id, language);
        tagNameRepository.delete(entity);
        return true;
    }



    /**
     * This method get all tag names
     * */
    public List<TagNameDTO> getAll() {
        Iterable<TagNameEntity> all = tagNameRepository.findAll();

        List<TagNameDTO> dtoList = new LinkedList<>();
        for (TagNameEntity entity : all) {
            dtoList.add(toDTO(entity));
        }
        return dtoList;
    }



    /**
     * This method sets the incoming entity to dto
     * */
    public TagNameDTO toDTO(TagNameEntity entity) {
        TagNameDTO dto = new TagNameDTO();
        dto.setId(entity.getId());
        dto.setTagName(entity.getTagName());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }



    /**
     * this method looks up the input ID from the database.
     * If found, it returns the found object, otherwise it throws an exception
     */
    public TagNameEntity get(Integer id, AppLanguage language) {
        return tagNameRepository.findById(id).orElseThrow(() -> {
            log.warn("Tag name not found {}", id);
            return new AppBadException(resourceBundleService.getMessage("tag.name.not.found", language));
        });
    }

}
