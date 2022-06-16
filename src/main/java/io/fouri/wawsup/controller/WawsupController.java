package io.fouri.wawsup.controller;

import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.errors.ResourceNotFoundException;
import io.fouri.wawsup.errors.UsernameAlreadyUsedException;
import io.fouri.wawsup.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequestMapping(value = "/api")
@RestController
public class WawsupController {
    @Autowired
    private UserService userService;

    /**
     * {@code GET  /} : API Running response
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        log.debug("[/] DEBUGGING Base Service");
        log.info("[/] Base Service Called");
        return "[/] WAWSUUUUUUUUUUPPPPP!!!!!!";
    }

    /**
     * {@code GET  /ping} : simple health-check response
     */
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        log.info("[/ping] Playing ping pong");
        return "pong";
    }

    /**
     * {@code GET  /user} : Retrieve a single user
     *
     * @param userName unique username for a given user
     * @throws ResponseStatusException {@code 404 (Not Found)} if user cannot be found.
     */
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@RequestParam(value = "userName") String userName) {
        log.info("[/user/{id}:getUser] Retrieving user: " + userName);
        try {
            return userService.getUser(userName);
        } catch (ResourceNotFoundException e) {
            log.error("[/user/{id}:getUser] User Not found: " + userName);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found: " + userName, e);
        }
    }

    /**
     * {@code POST  /user} : Create a new user
     *
     * @param newUser New User information - User POJO
     * @throws ResponseStatusException {@code 400 (Bad Request)} if username is already used
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user could not be created
     */
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User newUser) {
        log.info("[/user:createUser] Creating user: " + newUser.getUserName());
        try {
            userService.createUser(newUser);
        } catch (UsernameAlreadyUsedException e) {
            log.error("[/user:createUser] Username already exists, not created: " + newUser.getUserName());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username already exists, not created: " + newUser.getUserName(), e);
        }
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody User existingUser) {
        log.info("[/user:updateUser] Updating user: " + existingUser.getUserName());
        try {
            userService.updateUser(existingUser);
        } catch (ResourceNotFoundException e) {
            log.error("[/user:updateUser] Could not update user -- Not found: " + existingUser.getUserName());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Could not update user -- Not found: " + existingUser.getUserName(), e);
        }
    }
    @DeleteMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestParam(value = "userName") String userName) {
        log.info("[/user:deleteUser] Deleting user: " + userName);
        try {
            userService.deleteUser(userName);
        } catch (ResourceNotFoundException e) {
            log.error("[/user:deleteUser] Could not delete user -- Not found: " + userName);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Could not update user -- Not found: " + userName);
        }
    }
}
