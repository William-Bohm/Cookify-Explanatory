package videoStream.database.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "videos")
public class Video {
    @Id
    private String id;
    private String title;
    private String videoLength;
    private Long fileSize;
    private String contentType;
    private Instant lastModified;
    private String url;
    private String bucketName;
    private String thumbnailId;

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", videoLength='" + videoLength + '\'' +
                ", fileSize=" + fileSize +
                ", contentType='" + contentType + '\'' +
                ", lastModified=" + lastModified +
                ", url='" + url + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", thumbnailId='" + thumbnailId + '\'' +
                '}';
    }
}
