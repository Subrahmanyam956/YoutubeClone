package com.project.youtube.controller;

import com.project.youtube.dto.UploadVideoResponse;
import com.project.youtube.dto.VideoDto;
import com.project.youtube.model.VideoData;
import com.project.youtube.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file")MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file")MultipartFile file, @RequestParam("videoId") String id) {
        return videoService.uploadThumbnail(file, id);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetaData(@RequestBody VideoDto videoDto) throws IOException {
        return videoService.editVideo(videoDto);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoData getVideoById(@RequestParam("id")String id) throws IOException {
        return videoService.getVideoData(id);
    }
}
