package io.fouri.wawsup.service;

import io.fouri.wawsup.dao.DynamoDao;
import io.fouri.wawsup.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service class for managing users.  Simple implementation, should have more validation
 * TODO: Create environment specific DAO access
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private DynamoDao dao;

    public User getUser(String userName) throws NoSuchElementException {
        return dao.getUser(userName).orElseThrow();
    }

    public boolean deleteUser(String userName) {
        return dao.deleteUser(userName);
    }

    public boolean updateUser(User user) {
        return dao.updateUser(user);
    }

    public String createUser(User user) {
        log.debug("Processing new user: " + user.getUserName());
        User newUser = User.builder()
                .userName(user.getUserName().toLowerCase())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail().toLowerCase())
                .language(user.getLanguage())
                .imageUrl(user.getImageUrl())
                .createDate(DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()))
                .build();

        boolean result = dao.saveUser(newUser);
        if(result) {
            log.info("User Created: ", newUser);
        } else {
            log.error("There was a problem creating the user: " + newUser.getUserName());
        }

        return newUser.getUserName();
    }

}
