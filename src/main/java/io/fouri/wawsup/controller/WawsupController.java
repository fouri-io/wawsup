package io.fouri.wawsup.controller;

import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.errors.ResourceNotFoundException;
import io.fouri.wawsup.errors.UsernameAlreadyUsedException;
import io.fouri.wawsup.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        log.debug("[/] DEBUGGING Base Service");
        log.info("[/] Base Service Called");
        return "[/] WAWSUUUUUUUUUUPPPPP!!!!!!";
    }

    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String ping() {
        log.info("[/ping] Playing ping pong");
        userService.testDatabase();
        return "pong";
    }

    @Operation(summary = "Get User by username")
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@RequestParam(value = "userName") String userName) {
        log.info("[/user/{id}:getUser] Retrieving user: " + userName);
        try {
            return userService.getUser(userName);
        } catch (ResourceNotFoundException e) {
            log.error("[/user/{id}:getUser] User not found: " + userName);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found: " + userName, e);
        }
    }

    @Operation(summary = "Create User")
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
    @Operation(summary = "Update User")
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
    @Operation(summary = "Delete User by username")
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
