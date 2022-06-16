package io.fouri.wawsup.service;

import io.fouri.wawsup.dao.DynamoDao;
import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.errors.ResourceNotFoundException;
import io.fouri.wawsup.errors.UsernameAlreadyUsedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public User getUser(String userName) throws ResourceNotFoundException {
        return dao.getUser(userName).orElseThrow(ResourceNotFoundException::new);
    }

    public void deleteUser(String userName) {
        if(!dao.deleteUser(userName)) {
            throw new ResourceNotFoundException();
        }
    }

    public void updateUser(User user) {
        if(!dao.updateUser(user)) {
            throw new ResourceNotFoundException();
        }
    }

    public void createUser(User user) {
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
        if(!dao.createUser(newUser)) {
            throw new UsernameAlreadyUsedException();
        }
    }

}
