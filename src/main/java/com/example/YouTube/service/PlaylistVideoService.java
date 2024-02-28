package com.example.YouTube.service;

import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.repository.PlaylistVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistVideoService {

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    public void create(String videoId, List<Integer> playListVideo) {
        for (Integer play : playListVideo) {
            create(videoId, play);
        }
    }

    public void create(String videoId, Integer playListVideoId) {
        PlaylistVideoEntity entity = new PlaylistVideoEntity();
        entity.setVideoId(videoId);
        entity.setPlaylistId(playListVideoId);
        playlistVideoRepository.save(entity);
    }

}
