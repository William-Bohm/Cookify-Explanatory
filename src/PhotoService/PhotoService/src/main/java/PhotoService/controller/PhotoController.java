package PhotoService.controller;

import PhotoService.database.model.Photo;
import PhotoService.database.service.PhotoService;
import PhotoService.database.repository.PhotoRepository;
import PhotoService.service.Recipe.RecipeUploadService;
import PhotoService.service.retrieve.PhotoRetrieveService;
import PhotoService.service.upload.PhotoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoUploadService photoUploadService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoRetrieveService photoRetrieveService;

    @Autowired
    private RecipeUploadService recipeUploadService;

    @Value("${cookify.recipe.setPhoto}")
    private String addPhotoURL;

    @PostMapping("/upload")
    public Mono<ResponseEntity<?>> uploadPhoto(
            @RequestPart("title") String title,
            @RequestPart("recipeID") String recipeID,
            @RequestPart("photo") Mono<FilePart> file) {
        return photoUploadService.uploadPhoto(file)
                .flatMap(uploadResult -> {
                    try {
                        Photo savedPhoto = photoService.savePhotoToMongoDB(recipeID, title, uploadResult.getFileSize(), null, uploadResult.getBucketName(), uploadResult.getID(), uploadResult.getObjectUrl());

                        return recipeUploadService.addPhoto(recipeID, savedPhoto.getId())
                                .flatMap(statusCode -> {
                                    if (statusCode != 200) {
                                        return Mono.fromRunnable(() -> photoService.deletePhoto(savedPhoto.getId()))
                                                .then(Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Photo was uploaded but couldn't be associated with the recipe")));

                                    } else {
                                        return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(uploadResult));
                                    }
                                });

                    } catch (IOException e) {
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()));
                    }
                });
    }



    @GetMapping("/{photoID}")
    public Mono<Void> getPhoto(@PathVariable String photoID, ServerHttpResponse response) {
        return photoRetrieveService.getPhoto(photoID)
                .doOnSuccess(dataBuffer -> {
                    response.getHeaders().setContentType(MediaType.IMAGE_JPEG); // or any appropriate media type
                    response.writeWith(Mono.just(dataBuffer))
                            .doOnError(error -> response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR))
                            .subscribe();
                })
                .doOnError(error -> {
                    if (error instanceof ResponseStatusException) {
                        response.setStatusCode(((ResponseStatusException) error).getStatus());
                    } else {
                        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .then();
    }

    @GetMapping("/{photoId}/metadata")
    public Mono<Photo> getPhotoMetadata(@PathVariable String photoId) {
        return photoService.getPhotoMetadata(photoId)
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving photo metadata", e));
    }


}
