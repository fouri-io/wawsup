package io.fouri.wawsup.dao;

import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.errors.ResourceNotFoundException;
import io.fouri.wawsup.errors.UsernameAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Database Specific Dao -- Using Dynamo Single Table Design Pattern (aka Houlihan design)
 *
 * Reference:
 * - OG Rick Houlihan Intro to Single Table Design: https://www.youtube.com/watch?v=HaEPXoXVf2k
 * - Alex Debrie talk on Dynamo Design: https://www.youtube.com/watch?v=yNOVamgIXGQ
 * - Data Modeling with Amazon DynamoDB - Part 1: https://www.youtube.com/watch?v=fiP2e-g-r4g
 *  TODO: Implement Dyanamo -- In Memory for now
 */
@Slf4j
@Component
public class DynamoDao {
    private List<User> userList;

    public DynamoDao() {
        userList = new ArrayList<>();
    }

    public boolean createUser(User user) {
        if (findUser(user.getUserName()) == -1) {
            userList.add(user);
            log.debug("Added user {}", user);
            return true;
        } else {
           return false;
        }
    }

    private int findUser(String userName) {
        int foundUser = -1;
        for(int i=0; i < userList.size(); i++) {
            if(userList.get(i).getUserName().equals(userName)) {
                foundUser = i;
                break;
            }
        }
        log.debug("FindUser " + userName + " : " + foundUser);
        return foundUser;
    }

    public boolean deleteUser(String userName) {
        int userLocation = findUser(userName);
        if(userLocation != -1) {
            userList.remove(userLocation);
            log.debug("Deleted User: " + userName);
            return true;
        }
        log.debug("Could not find user to delete: " + userName);
        return false;
    }

    public Optional<User> getUser(String userName) {
        int userLocation = findUser(userName);
        if(userLocation != -1) {
            return Optional.of(userList.get(userLocation));
        }
        return Optional.empty();
    }

    public boolean updateUser(User user) {
        int userLocation = findUser(user.getUserName());
        if(userLocation != -1) {
            userList.remove(userLocation);
            userList.add(user);
            log.debug("Updated User {} ", user);
            return true;
        } else {
            log.debug("Could not find user to update: " + user.getUserName());
            return false;
        }
    }

}
