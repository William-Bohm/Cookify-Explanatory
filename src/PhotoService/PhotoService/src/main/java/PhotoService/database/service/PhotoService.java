package PhotoService.database.service;

import PhotoService.database.model.Photo;
import PhotoService.database.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo savePhotoToMongoDB(String recipeID, String title, Long photoSize, String thumbnailId, String bucketName, String ID, String url) throws IOException {
        // Create a Photo entity and save it to MongoDB
        Photo photo = new Photo();
        photo.setId(ID);
        photo.setTitle(title);
        photo.setPhotoSize(photoSize);
        photo.setUrl(url);
        photo.setBucketName(bucketName);
        photo.setThumbnailId(thumbnailId);
        photoRepository.save(photo);

        return photo;
    }

    public void deletePhoto(String photoId) {
        photoRepository.deleteById(photoId);
    }

    public Mono<Photo> getPhotoMetadata(String photoId) {
        return Mono.fromCallable(() -> {
            Optional<Photo> photoOptional = photoRepository.findById(photoId);
            if (photoOptional.isPresent()) {
                return photoOptional.get();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");
            }
        });
    }
}
