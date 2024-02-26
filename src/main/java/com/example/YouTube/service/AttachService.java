package com.example.YouTube.service;

import com.example.YouTube.dto.AttachDTO;
import com.example.YouTube.entity.AttachEntity;
import com.example.YouTube.enums.AppLanguage;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    @Autowired
    private ResourceBundleService resourceBundleService;


    /**
     * This method sends the image, video, or audio to the device with its address and name,
     * and throws an exception if the address is incorrect 👇🏻
     */
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
            entity.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());

            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * This method finds and returns the attachment by id, throws an exception if not found 👇🏻
     */
    public byte[] loadImage(String attachId, AppLanguage language) {
        String id = attachId.substring(0, attachId.lastIndexOf("."));
        AttachEntity entity = get(id, language);
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




    /**
     * This method is used to find the attachment by id and download it into the device memory 👇🏻
     */
    public ResponseEntity download(String attachId, AppLanguage language) {
        try {
            String id = attachId.substring(0, attachId.lastIndexOf("."));

            AttachEntity entity = get(id, language);

            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                log.warn("Could not read the file{}", attachId);
                throw new RuntimeException(resourceBundleService.getMessage("Could.not.read.the.file", language));
            }
        } catch (MalformedURLException e) {
            log.warn("Url wrong{}", e.getMessage());
            throw new RuntimeException(resourceBundleService.getMessage("Url.wrong", language));
        }
    }



    /**
     * This method used attach for pagination
     */
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



    /**
     * This method searches the database by attach id.
     * If found, it deletes the found object, otherwise it throws an exception 👇🏻
     */
    public Boolean delete(String id, AppLanguage language) {
        get(id, language);
        attachRepository.deleteById(id);
        return true;
    }



    /**
     * This method creates a separate directory for each day based on
     * the year, month, day based on the current time.
     * for example   ∨ year 2024
     * ∨ month 02
     * > day 1
     * > day 2
     * > day 3
     * ∨ month 03
     * ∨ day 1
     * photo1.jpg
     * photo2.jpg
     * > day 2
     * > day 3 👇🏻
     */
    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day;
    }


    /**
     * This method is used to get the file extension
     * if the filename is photo.jpg this method will return you "jpg 👇🏻"
     */
    public String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }


    /**
     * This method takes the data from the entity to the dto
     * sets id and url and returns 👇🏻
     */
    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        dto.setDuration(entity.getDuration());
        return dto;
    }


    /**This method takes the data and
     returns the url 👇🏻*/
    public AttachDTO toDTOForProfile(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        return dto;
    }


    /**
     * This method takes the data from the entity to the dto
     * sets id,url,size and original name and returns for the pagination 👇🏻
     */
    public AttachDTO toDTOPagination(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginalName());
        return dto;
    }


    /**
     * this method looks up the input ID from the database.
     * If found, it returns the found object, otherwise it throws an exception 👇🏻
     */
    public AttachEntity get(String id, AppLanguage language) {
        return attachRepository.findById(id).orElseThrow(() -> {
            log.warn("File not found{}", id);
            return new AppBadException(resourceBundleService.getMessage("File.not.found", language));
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