package videoStream.database.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import videoStream.database.model.Video;

import java.util.Optional;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Optional<Video> findById(String id);
    Video findByTitle(String title);
}
