package com.example.YouTube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "playlist_video")
public class PlaylistVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlist_id")
    private Integer playlistId;

    @ManyToOne
    @JoinColumn(name = "playlist_id",insertable = false,updatable = false)
    private PlaylistEntity playlist;

    @Column(name = "video_id")
    private String videoId;

    @ManyToOne
    @JoinColumn(name = "video_id",insertable = false,updatable = false)
    private VideoEntity video;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "oreder_number")
    private Integer orderNumber;
}
