package com.project.youtube.repository;

import com.project.youtube.model.VideoData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


// Here AmazonS3 is the VideoDataRepository which saved the videoData. The actual byte array of the Video data
@Repository
public interface VideoDataRepository extends MongoRepository<VideoData, String > {
}
