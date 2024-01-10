package PhotoService.service.upload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Getter
@Setter
public class PhotoUploadResult {
    private String eTag;
    private String objectUrl;
    private long fileSize;
    private String ID;
    private String bucketName;

    public PhotoUploadResult(String eTag, String objectUrl, long fileSize, String ID, String bucketName) {
        this.eTag = eTag;
        this.objectUrl = objectUrl;
        this.fileSize = fileSize;
        this.ID = ID;
        this.bucketName = bucketName;
    }
}

