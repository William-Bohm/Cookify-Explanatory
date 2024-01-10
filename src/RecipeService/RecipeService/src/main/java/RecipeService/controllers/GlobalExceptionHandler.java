package RecipeService.controllers;

import RecipeService.exceptions.RecipeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public Mono<ResponseEntity<String>> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<String>> handleValidationException(ServerWebInputException e) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
    }
}
