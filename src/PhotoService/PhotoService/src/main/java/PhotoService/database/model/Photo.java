package PhotoService.database.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;
    private String recipeID;
    private String title;
    private Long photoSize;
    private String url;
    private String bucketName;
    private String thumbnailId;
}
