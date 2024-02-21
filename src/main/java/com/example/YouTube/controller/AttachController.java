package com.example.YouTube.controller;

import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * This is used to create class attach methods
 */
@Tag(name = "Attach API list", description = "API list for Attach")
@RestController
@RequestMapping("/attach")
public class AttachController {

    @Autowired
    private AttachService attachService;


    /**
     * This method is used to upload the attachment
     **/
    @PostMapping("/upload")
    @Operation(summary = "Api for upload", description = "this api is used to upload attach")
    public ResponseEntity<AttachDTO> upload(@RequestParam(value = "file") MultipartFile file) {
        AttachDTO dto = attachService.save(file);
        return ResponseEntity.ok().body(dto);
    }


    /**
     * This method is used to get by filename the attachment
     **/
    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Api for to open file", description = "this api is used to loadImage")
    public byte[] open(@PathVariable("fileName") String fileName,
                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                return this.attachService.loadImage(fileName, language);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }


    /**
     * This method is used to download the attachment
     **/
    @GetMapping("/download/{fineName}")
    @Operation(summary = "Api for download", description = "this api is used to download attach")
    public ResponseEntity<Resource> download(@PathVariable("fineName") String fileName,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return attachService.download(fileName, language);
    }


    /**
     * This method is used by the Admin to pagination the attachment
     **/
    @GetMapping("/attachPagination")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Api for pagination", description = "this api is used to pagination attach")
    public ResponseEntity<PageImpl<AttachDTO>> pagination(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "2") Integer size) {
        return ResponseEntity.ok(attachService.getAttachPagination(page, size));
    }


    /**
     * This method is used by the admin to delete the attach by id
     */
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Api for delete", description = "this api is used to delete attach")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String id,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ResponseEntity.ok(attachService.delete(id, language));
    }


}