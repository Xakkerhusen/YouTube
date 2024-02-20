
package com.example.YouTube.controller;

import com.example.YouTube.dto.CategoryDTO;
import com.example.YouTube.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping("/adm/create")
    @Operation(summary = "Api for create", description = "this api is used to create category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CategoryDTO dto) {
        log.info("Create category {}", dto.getName());
        return ResponseEntity.ok(categoryService.create(dto));
    }


}
