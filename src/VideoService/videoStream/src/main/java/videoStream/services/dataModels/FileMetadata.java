package videoStream.services.dataModels;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class FileMetadata {
    private final long size;
    private final String contentType;
    private final Instant lastModified;

    public FileMetadata(long size, String contentType, Instant lastModified) {
        this.size = size;
        this.contentType = contentType;
        this.lastModified = lastModified;
    }
}




