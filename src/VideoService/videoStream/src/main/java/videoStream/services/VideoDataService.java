package videoStream.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import videoStream.services.dataModels.FileMetadata;

@Service
public class VideoDataService {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public Mono<FileMetadata> getVideoMetadata(String videoID) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(videoID)
                .build();
        return Mono.fromCallable(() -> s3Client.headObject(headObjectRequest))
                .map(response -> new FileMetadata(response.contentLength(), response.contentType(), response.lastModified()))
                .onErrorResume(e -> {
                    // Log error
                    System.err.println(e.getMessage());
                    return Mono.error(e);
                });
    }
}
