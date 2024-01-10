package videoStream.services.dataModels;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Getter
@Setter
public class VideoUploadResult {
    PutObjectResponse putObjectResponse;
    String bucketName;
    String videoID;

    public VideoUploadResult(PutObjectResponse putObjectResponse, String bucketName, String videoID) {
        this.putObjectResponse = putObjectResponse;
        this.bucketName = bucketName;
        this.videoID = videoID;
    }
}
