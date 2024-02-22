package com.example.YouTube.entity;

import com.example.YouTube.enums.ProfileRole;
import com.example.YouTube.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity{
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProfileStatus status=ProfileStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;

    @Column(name = "main_photo")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_photo",insertable = false,updatable = false)
    private AttachEntity attach;
}
