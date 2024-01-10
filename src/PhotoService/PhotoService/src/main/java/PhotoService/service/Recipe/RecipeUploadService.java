package PhotoService.service.Recipe;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RecipeUploadService {

    private final WebClient webClient;

    public RecipeUploadService() {
        this.webClient = WebClient.create("insert URI here");
    }

    public Mono<Integer> addPhoto(String recipeId, String photo) {
        String url = "/cookify/recipes/{id}/photos";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return webClient.post()
                .uri(url, recipeId)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(photo)
                .exchange()
                .map(response -> response.rawStatusCode());
    }
}
