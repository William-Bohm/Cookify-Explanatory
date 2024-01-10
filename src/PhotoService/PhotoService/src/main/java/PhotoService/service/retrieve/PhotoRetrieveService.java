package PhotoService.service.retrieve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class PhotoRetrieveService {
    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public Mono<DataBuffer> getPhoto(String photoID) {
        return Mono.create(sink -> {
            try {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(photoID)
                        .build();

                ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(getObjectRequest);
                byte[] data = objectBytes.asByteArray();
                DataBuffer buffer = new DefaultDataBufferFactory().wrap(data);

                sink.success(buffer);
            } catch (S3Exception e) {
                sink.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));
            } catch (Exception e) {
                sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving photo"));
            }
        });
    }

}
