package com.example.YouTube.entity;

import com.example.YouTube.enums.ReportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "report")
public class ReportEntity {
//    15. report
//    id,profile_id,content,entity_id(channel_id,video_id),type(channel,video)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;

    @Column(name = "profile_id")
    private Integer profileID;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;

    @Column(name = "entity_id")
    private String entityID;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ReportType type;










}
