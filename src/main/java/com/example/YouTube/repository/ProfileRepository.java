package com.example.YouTube.repository;

import com.example.YouTube.entity.ProfileEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {
    Optional<ProfileEntity> findByEmail(String username);
    Optional<ProfileEntity> findById(Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set password=?2 where id=?1")
    Integer updatePassword(Integer profileId, String password);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String encode);
}
