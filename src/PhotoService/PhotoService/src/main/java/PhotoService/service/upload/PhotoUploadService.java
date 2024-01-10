package PhotoService.service.upload;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class PhotoUploadService {
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    public Mono<PhotoUploadResult> uploadPhoto(Mono<FilePart> filePartMono) {
        return filePartMono.flatMap(filePart -> {
            String photoID = generateID();
            String fileName = filePart.filename();

            return DataBufferUtils.join(filePart.content())
                    .flatMap(dataBuffer -> {
                        ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(photoID)
                                .build();
                        return Mono.fromCallable(() -> s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(byteBuffer)))
                                .doOnNext(response -> DataBufferUtils.release(dataBuffer))
                                .flatMap(putObjectResponse -> {
                                    String objectUrl = "https://" + bucketName + ".s3.amazonaws.com/" + photoID;

                                    // Now we fetch the size of the uploaded file
                                    HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                                            .bucket(bucketName)
                                            .key(photoID)
                                            .build();
                                    HeadObjectResponse objectHead = s3Client.headObject(headObjectRequest);
                                    long size = objectHead.contentLength();

                                    return Mono.just(new PhotoUploadResult(putObjectResponse.eTag(), objectUrl, size, photoID, bucketName));
                                });
                    });
        });
    }


    public boolean deletePhotoFromS3(String bucketName, String photoId) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(photoId)
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
