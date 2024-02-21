package com.example.YouTube.entity;

import com.example.YouTube.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "channel")
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "description",columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status=Status.ACTIVE;
    @Column(name = "banner_id")
    private String bannerId;
    @Column(name = "profile_id")
    private Integer profileId;
    @Column(name = "visible")
    private Boolean visible=true;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id",insertable = false,updatable = false)
    private AttachEntity photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id",insertable = false,updatable = false)
    private AttachEntity Banner;
}
