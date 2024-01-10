package videoStream.services;


import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;
import videoStream.services.dataModels.VideoUploadResult;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Service
public class VideoUploadService {
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public Mono<VideoUploadResult> uploadTranscodedCopy(File file) {
        String fileName = file.getName();
        Path path = file.toPath();

        return Mono.fromCallable(() -> Files.readAllBytes(path))
                .flatMap(data -> {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    String videoID = generateID();
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(videoID)
                            .build();

                    return Mono.fromCallable(() -> s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(byteBuffer)))
                            .map(putObjectResponse -> new VideoUploadResult(putObjectResponse, bucketName, videoID));
                })
                .onErrorResume(e -> {
                    // Log error
                    System.err.println(e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<VideoUploadResult> uploadVideo(FilePart filePart) {
        String fileName = filePart.filename();

        return DataBufferUtils.join(filePart.content())
                .flatMap(dataBuffer -> {
                    ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                    String videoID = generateID();
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(videoID)
                            .build();

                    return Mono.fromCallable(() -> s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(byteBuffer)))
                            .doOnNext(response -> DataBufferUtils.release(dataBuffer))
                            .map(putObjectResponse -> new VideoUploadResult(putObjectResponse, bucketName, videoID));
                })
                .onErrorResume(e -> {
                    // Log error
                    System.err.println(e.getMessage());
                    return Mono.error(e);
                });
    }



//    public VideoUploadResult uploadVideo(MultipartFile file) throws IOException {
//        String fileName = file.getOriginalFilename();
//
//        try {
//            String videoID = generateID();
//            ByteBuffer fileByteBuffer = ByteBuffer.wrap(file.getBytes());
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(videoID)
//                    .build();
//            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(fileByteBuffer));
//            return new VideoUploadResult(putObjectResponse, bucketName, videoID);
//        } catch (S3Exception e) {
//            // Log error
//            System.err.println(e.awsErrorDetails().errorMessage());
//            throw e;
//        }
//    }

    public boolean deleteVideoFromS3(String bucketName, String videoId) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(videoId)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            return true; // Return true on successful deletion
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return false; // Return false on failure
        }
    }

    private String generateID() {
        return UUID.randomUUID().toString();
    }


}
