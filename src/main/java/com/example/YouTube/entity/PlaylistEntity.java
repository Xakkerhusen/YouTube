package com.example.YouTube.entity;

import com.example.YouTube.enums.PlaylistStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "playlist")
public class PlaylistEntity extends BaseEntity {

    @Column(name = "channel_id")
    private String channelId;

    @ManyToOne
    @JoinColumn(name = "channel_id",insertable = false,updatable = false)
    private ChannelEntity channel;
    @Column(name = "name")
    private String name;
    @Column(name = "description",columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PlaylistStatus status;
    @Column(name = "order_number")
    private Integer orderNumber;

}
