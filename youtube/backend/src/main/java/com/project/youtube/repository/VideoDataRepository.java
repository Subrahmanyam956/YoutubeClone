package com.project.youtube.repository;

import com.project.youtube.model.VideoData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoDataRepository extends MongoRepository<VideoData, String > {
}
