package videoStream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import videoStream.database.model.Video;
import videoStream.database.repository.VideoRepository;
import videoStream.database.service.VideoService;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import videoStream.services.TranscodeService;
import videoStream.services.VideoStreamService;
import videoStream.services.dataModels.VideoUploadResult;
import videoStream.services.VideoUploadService;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    private static final Logger log = LoggerFactory.getLogger(VideoController.class);

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private TranscodeService transcodeService;


    @Autowired
    private VideoUploadService videoUploadService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoStreamService videoStreamService;

    // This method handles the video upload endpoint ("/upload") in a Java Spring application.
    // It takes a file part and a video title as request parameters.
    // The file part is uploaded asynchronously using the videoUploadService.uploadVideo() method.
    // If the upload is successful, the method saves the video information to MongoDB and returns a success response.
    // If the upload fails or the database save fails, appropriate error responses are returned, and the video is deleted from S3 if necessary.
    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> uploadVideo(@RequestPart("file") Mono<FilePart> filePartMono, @RequestPart("videoTitle") String videoTitle) {
        return filePartMono
                .flatMap(filePart -> {
                    Mono<VideoUploadResult> uploadResultMono = videoUploadService.uploadVideo(filePart);

                    return uploadResultMono.flatMap(uploadResult -> {
                        // Check PutObjectResponse for success or failure
                        if (!uploadResult.getPutObjectResponse().sdkHttpResponse().isSuccessful()) {
                            log.info("failed upload request");
                            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Failed to upload " + filePart.filename()));
                        }

                        try {
                            Video createdVideo = videoService.saveVideoToMongoDB(videoTitle, null, null,
                                    uploadResult.getBucketName(), uploadResult.getVideoID());

                            log.info("Video upload succesful");
                            return Mono.just(ResponseEntity.status(HttpStatus.OK)
                                    .body("Successfully uploaded " + filePart.filename() + "\n" + createdVideo.toString()));
                        } catch (Exception e) {
                            // If database save fails, delete video from S3
                            videoUploadService.deleteVideoFromS3(uploadResult.getBucketName(), uploadResult.getVideoID());

                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Failed to upload " + filePart.filename() + "\n" + e.getMessage()));
                        }
                    });
                });
    }

    // This method handles the video deletion endpoint ("/delete/{id}") in a Java Spring application.
    // It takes the video ID as a path variable and attempts to delete the corresponding video.
    // First, it retrieves the video from the video repository using the ID.
    // If the video exists, it attempts to delete the video from S3 using the videoUploadService.deleteVideoFromS3() method.
    // If the S3 deletion is successful, the video is also deleted from the database using the videoService.deleteVideo() method, and a success response is returned.
    // If the S3 deletion fails, the video is not deleted from the database, and an appropriate error response is returned.
    // If the video is not found in the repository, a not found response is returned.
    // If an exception occurs during the deletion process, an error response is returned along with the exception message.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable String id) {
        try {
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (videoOptional.isPresent()) {
                Video video = videoOptional.get();
                boolean s3DeletionStatus = videoUploadService.deleteVideoFromS3(video.getBucketName(), video.getId());
                if (s3DeletionStatus) {
                    // Delete from the database only if S3 deletion was successful
                    videoService.deleteVideo(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Video deleted successfully");
                } else {
                    // S3 deletion was not successful, hence don't delete from the database
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete video from S3");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No video found with the given ID");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete video\n" + e.getMessage());
        }
    }


    // Streams a video with the specified ID.
    // Attempts to stream the video from the "myBucket" bucket using the videoStreamService.
    // Supports partial content retrieval using the "Range" header if provided.
    // Returns a Mono that emits a ResponseEntity with the video stream as an InputStreamResource.
    // If an exception occurs during the streaming process, logs the error and returns an internal server error response.
    @GetMapping("/stream/{videoId}")
    public Mono<ResponseEntity<InputStreamResource>> streamVideoVariableRange(@PathVariable String videoId,
                                                                 @RequestHeader(value = "Range", required = false) String range) {
        try {
            return videoStreamService.streamVideo(bucketName, videoId, range);
        } catch (Exception e) {
            log.error("Error occurred when trying to stream video: {}", e.getMessage(), e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }


    @GetMapping("/stream/{videoId}")
    public Mono<ResponseEntity<InputStreamResource>> streamVideoStandard(@PathVariable String videoId,
                                                                              @RequestHeader(value = "Range", required = false) String range) {
        try {
            return videoStreamService.streamVideo(bucketName, videoId, range);
        } catch (Exception e) {
            log.error("Error occurred when trying to stream video: {}", e.getMessage(), e);
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }
}
