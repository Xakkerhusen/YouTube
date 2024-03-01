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
    @ManyToOne
    @JoinColumn(name = "video_id", insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "tag_id")
    private Integer tagId;
    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private TagNameEntity tag;

    @Column(name = "tag_name")
    private String tagName;
}
