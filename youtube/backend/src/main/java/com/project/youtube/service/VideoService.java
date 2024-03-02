package com.project.youtube.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.project.youtube.dto.UploadVideoResponse;
import com.project.youtube.dto.VideoDto;
import com.project.youtube.model.Video;
import com.project.youtube.model.VideoData;
import com.project.youtube.repository.VideoDataRepository;
import com.project.youtube.repository.VideoRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class VideoService {

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;
    private final VideoDataRepository videoDataRepository;
    private final VideoRepository videoRepository;

    public VideoService(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations, VideoDataRepository videoDataRepository, VideoRepository videoRepository) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
        this.videoDataRepository = videoDataRepository;
        this.videoRepository = videoRepository;
    }

    public UploadVideoResponse uploadVideo(MultipartFile file) {
        String videoName = null;
        Video video;
        UploadVideoResponse uploadVideoResponse = null;
        try {
            // uplaod file to Mongo
            String title = UUID.randomUUID().toString();
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "video");
            metaData.put("title", title);
            metaData.put("fileSize", file.getSize());

            String videoDataId = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData).toString();
            video = new Video();
            Video savedVideo = videoRepository.save(video);
            uploadVideoResponse = new UploadVideoResponse(savedVideo.getId(), videoDataId);

//            videoName = videoDataId + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return uploadVideoResponse;
    }

    public VideoData getVideoData(String id) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        VideoData video = new VideoData();
        if(null != file && null != file.getMetadata()) {
            video.setTitle(file.getMetadata().get("title").toString());
            video.setInputStream(gridFsOperations.getResource(file).getInputStream());
            video.getInputStream().close();
        }
        return video;
    }

    public Video getVideoById(String id) {
        return videoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cannot find video by id - "+id));
    }

    public VideoDto editVideo(VideoDto videoDto) throws IOException {
        // find the Video by videoId
        Video video = getVideoById(videoDto.getId());

        VideoData videoData = this.getVideoData(videoDto.getId());

        video.setTitle(videoDto.getTitle());
        video.setDescription(videoDto.getDescription());
        // video.setUrl(videoMetaDataDto.getUrl());
        // Ignoring video ID as it should not be possible to change the Channel of a Video
        video.setTags(videoDto.getTags());
        video.setVideoStatus(videoDto.getVideoStatus());
        // View Count is also ignored as its calculated independently
        videoRepository.save(video);

        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        Video savedVideo = getVideoById(videoId);
        // upload the thumbnail multipart file
        UploadVideoResponse uploadVideoResponse = uploadVideo(file);
        String thumbnailName = uploadVideoResponse.getVideoId();

//        String[] name = thumbnailName.split("\\.");
//        String thumbnailUrl = name[0];
        savedVideo.setThumbnailUrl(thumbnailName);
        videoRepository.save(savedVideo);
        return thumbnailName;
    }
}
