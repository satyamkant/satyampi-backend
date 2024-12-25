package uk.satyampi.BlogMs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCtrl {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("BLog microservice is up and running", HttpStatus.OK);
    }
}