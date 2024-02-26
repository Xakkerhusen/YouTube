package com.example.YouTube.service;

import com.example.YouTube.entity.PlaylistVideoEntity;
import com.example.YouTube.repository.PlaylistVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistVideoService {

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

}
