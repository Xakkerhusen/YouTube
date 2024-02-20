package com.example.YouTube.service;

import com.example.YouTube.dto.CategoryDTO;
import com.example.YouTube.entity.CategoryEntity;
import com.example.YouTube.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        CategoryEntity entity=new CategoryEntity();
        entity.setCreatedDate(LocalDateTime.now());
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        return dto;


    }
}
