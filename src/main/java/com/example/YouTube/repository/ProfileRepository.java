package com.example.YouTube.repository;

import com.example.YouTube.entity.ProfileEntity;
import com.example.YouTube.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmail(String username);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set password=?2 where id=?1")
    Integer updatePassword(Integer profileId, String password);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String encode);

    @Transactional
    @Modifying
    @Query("Update ProfileEntity  set status =?2 where id = ?1")
    void updateStatus(Integer id, ProfileStatus profileStatus);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set email=?2 where id=?1")
    void updateEmail(Integer id, String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set attachId=?2 where id=?1")
    Integer updatePhoto(Integer profileId, String id);
}
