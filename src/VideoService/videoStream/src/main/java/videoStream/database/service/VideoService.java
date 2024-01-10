package videoStream.database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import videoStream.database.model.Video;
import videoStream.database.repository.VideoRepository;
import videoStream.services.VideoDataService;
import videoStream.services.dataModels.FileMetadata;

import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoDataService videoDataService;

    public Video saveVideoToMongoDB(String title, String videoLength, String thumbnailId, String bucketName, String ID) throws IOException {
        FileMetadata metadata = videoDataService.getVideoMetadata(ID).block();
        assert metadata != null;
        Video video = new Video();
        video.setId(ID);
        video.setTitle(title);
        video.setVideoLength(videoLength);
        video.setFileSize(metadata.getSize());
        video.setUrl(bucketName + "/" + ID);
        video.setContentType(metadata.getContentType());
        video.setLastModified(metadata.getLastModified());
        video.setBucketName(bucketName);
        video.setThumbnailId(thumbnailId);
        videoRepository.save(video);

        return video;
    }

    public void deleteVideo(String videoId) {
        videoRepository.deleteById(videoId);
    }


}
