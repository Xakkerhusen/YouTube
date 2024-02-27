package com.example.YouTube.service;

import com.example.YouTube.dto.CreateReportDTO;
import com.example.YouTube.dto.ReportInfoDTO;
import com.example.YouTube.entity.CategoryEntity;
import com.example.YouTube.entity.ReportEntity;
import com.example.YouTube.enums.AppLanguage;
import com.example.YouTube.exp.AppBadException;
import com.example.YouTube.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportService {
    @Autowired
    private ReportRepository repository;

    @Autowired
    private ResourceBundleService service;

    /**
     * a report is created from the data that came through this method.
     */
    public String create(Integer profileID, CreateReportDTO dto, AppLanguage language) {
        ReportEntity entity = new ReportEntity();
        entity.setProfileID(profileID);
        entity.setContent(dto.getContent());
        entity.setType(dto.getType());
        entity.setEntityID(dto.getType() + ": " + dto.getEntityID());
        repository.save(entity);
        String message = service.getMessage("report.create", language);
        return message;
    }






    /**
     * through this method, ADMIN can Page the generated reports.
     * In doing so, size and page enter the method.
     */

    public PageImpl<ReportInfoDTO> getList(Integer size, Integer page, AppLanguage language) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<ReportEntity> reportEntity = repository.findAll(pageable);
        List<ReportEntity> entityList = reportEntity.getContent();

        long totalElements = reportEntity.getTotalElements();
        List<ReportInfoDTO> dtoList = new ArrayList<>();

        for (ReportEntity entity : entityList) {
            dtoList.add(dto(entity));
        }
        return new PageImpl<>(dtoList, pageable, totalElements);

    }




    private ReportInfoDTO dto(ReportEntity entity) {
        ReportInfoDTO dto = new ReportInfoDTO();
        dto.setReportID(entity.getId());
        dto.setProfileName(entity.getProfile().getName());
        dto.setProfileSurname(entity.getProfile().getSurname());
        dto.setType(entity.getType());
        dto.setEntityID(entity.getEntityID());
        dto.setProfileID(entity.getProfileID());
        dto.setContent(entity.getContent());
        dto.setProfilePhotoID(entity.getProfile().getAttachId());
        return dto;
    }


    /**
     * through this method, ADMIN will delete the existing Report report.
     * if this report is not available in the data warehouse, an error occurs
     */
    public Boolean removeReport(Integer reportId, AppLanguage lan) {
        ReportEntity entity = get(reportId, lan);
        repository.delete(entity);
        return true;

    }



    public ReportEntity get(Integer id, AppLanguage language) {
        Optional<ReportEntity> optional = repository.findById(id);
        if (optional.isEmpty()) {
            String message = service.getMessage("report.not.found", language);
            log.warn(message);
            throw new AppBadException(message);
        }
        return optional.get();
    }



    /**
     * through this method, the ADMIN user can view
     * the accounts created by him through the userId in a list view.
     */
    public List<ReportInfoDTO> getReportList(Integer profileId, AppLanguage language) {
        List<ReportEntity> list = getProfileID(profileId, language);
        List<ReportInfoDTO> reportList = new ArrayList<>();
        for (ReportEntity entity : list) {
            reportList.add(dto(entity));
        }
        return reportList;
    }



    private List<ReportEntity> getProfileID(Integer profileId, AppLanguage language) {
        List<ReportEntity> list = repository.getReportProfileID(profileId);
        if (list.isEmpty()) {
            String message = service.getMessage("report.profileId.not.fount", language);
            log.warn(message);
            throw new AppBadException(message);
        }
        return list;

    }
}
