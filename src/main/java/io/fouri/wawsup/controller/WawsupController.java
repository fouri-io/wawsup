package io.fouri.wawsup.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class WawsupController {
    @GetMapping("/")
    public ResponseEntity<Object> index() {
        log.debug("[/] DEBUGGING Base Service");
        String response = "[/] WAWSUUUUUUUUUUPPPPP!!!!!!";
        log.info("[/] Base Service Called");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/ping")
    public ResponseEntity<Object> ping() {
        String response = "pong";
        log.info("[/ping] Playing ping pong");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
