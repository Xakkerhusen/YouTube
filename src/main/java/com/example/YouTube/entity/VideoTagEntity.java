package com.example.YouTube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "video_tag")
public class VideoTagEntity extends BaseEntity{
    @Column(name = "video_id")
    private String videoId;
    @Column(name = "tag_id")
    private Integer tagId;
}
