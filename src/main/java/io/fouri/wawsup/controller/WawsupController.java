package io.fouri.wawsup.controller;

import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@Slf4j
@RestController
public class WawsupController {
    @Autowired
    private UserService userService;

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

    @GetMapping("/user")
    public ResponseEntity<Object> getUser(@RequestParam(value = "userName") String userName) {
        log.info("[/user/{id}] Retrieving user: " + userName);
        try {
            return new ResponseEntity<>(userService
                    .getUser(userName),
                    HttpStatus.FOUND);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("User not found with username: " + userName,
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        log.info("[/user] Creating user: " + newUser.getUserName());
        userService.createUser(newUser);
        return new ResponseEntity<>("User Service created: " + newUser.getUserName(), HttpStatus.OK);
    }
}
