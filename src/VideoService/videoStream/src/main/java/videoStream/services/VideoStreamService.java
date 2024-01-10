package videoStream.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.core.sync.ResponseTransformer;

@Service
public class VideoStreamService {
    private static final Logger log = LoggerFactory.getLogger(VideoStreamService.class);


    @Autowired
    private S3Client s3Client;

    public Mono<ResponseEntity<InputStreamResource>> streamVideo(String bucketName, String videoId, String range) {
        return Mono.fromCallable(() -> {
            GetObjectRequest.Builder getObjectRequestBuilder = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(videoId);

            if (range != null && !range.isEmpty()) {
                getObjectRequestBuilder.range(range);
            }

            GetObjectRequest getObjectRequest = getObjectRequestBuilder.build();
            // Log the GetObjectRequest details
            log.info("GetObjectRequest: {}", getObjectRequest);
            ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
            // Log the GetObjectResponse details
            log.info("GetObjectResponse: {}", objectData.response());
            long contentLength = objectData.response().contentLength();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "video/mp4");
            headers.add("Content-Length", String.valueOf(contentLength));
            headers.add("Accept-Ranges", "bytes");

            InputStreamResource resource = new InputStreamResource(objectData);

            if (range == null || range.isEmpty()) {
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                String[] ranges = range.split("=")[1].split("-");
                long rangeStart = Long.parseLong(ranges[0]);
                long rangeEnd;
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                } else {
                    rangeEnd = contentLength - 1;
                }
                long rangeLength = rangeEnd - rangeStart + 1;
                headers.add("Content-Length", String.valueOf(rangeLength));
                headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + contentLength);

                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .headers(headers)
                        .body(resource);
            }
        }).onErrorResume(S3Exception.class, e -> {
            log.error("Error occurred when trying to retrieve video from S3: {}", e.awsErrorDetails().errorMessage());
            return Mono.just(ResponseEntity.notFound().build());
        });
    }
}
