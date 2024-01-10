package PhotoService.database.repository;

import PhotoService.database.model.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    Optional<Photo> findById(String id);
    Photo findByTitle(String title);
}
