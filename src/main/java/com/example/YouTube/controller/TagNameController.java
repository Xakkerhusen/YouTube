package com.example.YouTube.controller;

import com.example.YouTube.dto.TagNameDTO;
import com.example.YouTube.service.TagNameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag Name API list", description = "API list for Tag Name")
@RestController
@RequestMapping("/tagName")
public class TagNameController {

    @Autowired
    private TagNameService tagNameService;

    @PostMapping("/adm")
    @Operation(summary = "Api for create", description = "this api is used to create tag name")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TagNameDTO> create(@RequestBody TagNameDTO dto) {
        return ResponseEntity.ok(tagNameService.create(dto));
    }

    @PutMapping("/update/adm/{id}")
    @Operation(summary = "Api for update", description = "this api is used to update tag name")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> update(@PathVariable("id") Integer id, @RequestBody TagNameDTO dto) {
        return ResponseEntity.ok(tagNameService.update(id, dto));
    }

    @DeleteMapping("/delete/adm/{id}")
    @Operation(summary = "Api for delete", description = "this api is used to delete tag name")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(tagNameService.delete(id));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Api for get all tag name", description = "this api is used to get all tag name")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TagNameDTO>> getAll() {
        return ResponseEntity.ok(tagNameService.getAll());
    }





}
