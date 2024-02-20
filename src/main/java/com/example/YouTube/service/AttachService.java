package com.example.YouTube.service;

import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.AttachRepository;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    @Value("${server.url}")
    private String serverUrl;


    public AttachDTO save(MultipartFile file) {
        try {
            String pathFolder = getYmDString();
            File folder = new File("uploads/" + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String key = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());

            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + pathFolder + "/" + key + "." + extension);

            Files.write(path, bytes);

            long durationInSeconds = getDurationInSeconds(path.toAbsolutePath());

            AttachEntity entity = new AttachEntity();
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedData(LocalDateTime.now());
            entity.setId(key);
            entity.setPath(pathFolder);
            entity.setDuration(durationInSeconds);

            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public byte[] loadImage(String attachId) {
        String id = attachId.substring(0, attachId.lastIndexOf("."));
        AttachEntity entity = get(id);
        byte[] data;
        try {
            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public ResponseEntity download(String attachId) {
        try {
            String id = attachId.substring(0, attachId.lastIndexOf("."));

            AttachEntity entity = get(id);

            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                log.warn("Could not read the file!");
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            log.warn("Error {}",e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public PageImpl<AttachDTO> getAttachPagination(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdData");

        Pageable paging = PageRequest.of(page - 1, size, sort);

        Page<AttachEntity> attachPage = attachRepository.findAll(paging);
        List<AttachEntity> entityList = attachPage.getContent();
        long totalElement = attachPage.getTotalElements();

        List<AttachDTO> dtoList = new LinkedList<>();
        for (AttachEntity entity : entityList) {
            dtoList.add(toDTOPagination(entity));
        }
        return new PageImpl<>(dtoList, paging, totalElement);
    }

    public Boolean delete(String id) {
        AttachEntity attachEntity = get(id);
        attachRepository.delete(attachEntity);
        return true;
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day;
    }

    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);

    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        dto.setDuration(entity.getDuration());
        return dto;
    }

    public AttachDTO toDTOPagination(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginalName());
        return dto;
    }


    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            log.warn("File not found{}", id);
            return new AppBadException("File not found");
        });
    }

    public  long getDurationInSeconds(Path videoFilePath) {
        Path absolutePath = videoFilePath.toAbsolutePath();
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(absolutePath.toString())) {
            grabber.start();
            long durationMicroseconds = grabber.getLengthInTime();
            return durationMicroseconds / 1_000_000; // Microseconds to seconds
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
            return -1; // Indicate error with negative value
        }
    }

}