package com.example.YouTube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "tags")
public class TagNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name",nullable = false, unique = true)
    private String tagName;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
