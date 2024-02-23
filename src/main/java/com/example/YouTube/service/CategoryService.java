package com.example.YouTube.service;

import com.example.YouTube.dto.CategoryDTO;
import com.example.YouTube.entity.CategoryEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ResourceBundleService bundleService;

    @Autowired
    private ResourceBundleService resourceBundleService;

    /**
     * This method is structured to create category,
     * and the category can only be created by Admin. 👇🏻
     */
    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedDate(LocalDateTime.now());
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        return dto;
    }


    /**
     * This method was compiled to change the name of the created category .
     * For this method to work, a Category id must be given, and this work is only done by Admin. 👇🏻
     */
    public String update(Integer categoryId, CategoryDTO dto, AppLanguage lan) {
        Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);

        if (optional.isEmpty()) {
            log.warn("There is no category with such an id.Please check categoryId");
            String message = bundleService.getMessage("category.not.found", lan);
            throw new AppBadException(message);
        }
        CategoryEntity entity = optional.get();
        entity.setName(dto.getName());
        entity.setUpdateDate(LocalDateTime.now());
        categoryRepository.save(entity);
        return "update successful";
    }


    /**
     * This method is configured to delete an existing directory via categoryId ,
     * and the category can only be deleted by Admin. 👇🏻
     */
    public Boolean delete(Integer categoryId, AppLanguage language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(categoryId);
        if (optional.isEmpty()) {
            log.warn("There is no category with such an id.Please check categoryId");
            String message = bundleService.getMessage("category.not.found", language);
            throw new AppBadException(message);
        }
        categoryRepository.deleteById(categoryId);
        return true;

    }


    /**
     * This method returns all the categories created in the list view. 👇🏻
     */
    public List<CategoryDTO> getList() {
        Iterable<CategoryEntity> categoryList = categoryRepository.findAll();

        List<CategoryDTO> list = new LinkedList<>();
        for (CategoryEntity entity : categoryList) {
            CategoryDTO dto = new CategoryDTO();
            dto.setName(entity.getName());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setUpdateDate(entity.getUpdateDate());
            list.add(dto);
        }
        return list;
    }

    public CategoryEntity get(Integer id, AppLanguage language) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new AppBadException(resourceBundleService.getMessage("Category.not.found", language));
        }
        return optional.get();
    }

}
