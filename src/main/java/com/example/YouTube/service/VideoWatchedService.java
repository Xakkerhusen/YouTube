package com.example.YouTube.service;

import com.example.YouTube.entity.VideoWatchedEntity;
import com.example.YouTube.repository.VideoWatchedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoWatchedService {

    @Autowired
    private VideoWatchedRepository videoWatchedRepository;
    public Boolean save(String videoId, Integer profileId) {
        VideoWatchedEntity videoWatched = new VideoWatchedEntity();
        videoWatched.setVideoId(videoId);
        videoWatched.setProfileId(profileId);
        videoWatchedRepository.save(videoWatched);
        return true;
    }

}
