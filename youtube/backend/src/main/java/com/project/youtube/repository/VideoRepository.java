package com.project.youtube.repository;

import com.project.youtube.model.Video;
import com.project.youtube.model.VideoData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    List<Video> findByUserId(String userId);

    List<Video> findByTagsIn(List<String> tags);

    List<Video> findByIdIn(Set<String> likedVideos);
}
