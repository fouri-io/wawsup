package io.fouri.wawsup.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import io.fouri.wawsup.domain.User;
import io.fouri.wawsup.domain.WawsupTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Database Specific Dao -- Using Dynamo Single Table Design Pattern (aka Houlihan design)
 *
 * Reference:
 * - OG Rick Houlihan Intro to Single Table Design: https://www.youtube.com/watch?v=HaEPXoXVf2k
 * - Alex Debrie talk on Dynamo Design: https://www.youtube.com/watch?v=yNOVamgIXGQ
 * - Data Modeling with Amazon DynamoDB - Part 1: https://www.youtube.com/watch?v=fiP2e-g-r4g
 * - https://aws.amazon.com/blogs/database/amazon-dynamodb-single-table-design-using-dynamodbmapper-and-spring-boot/
 */
@Slf4j
@Component
public class DynamoDao {
    @Value("${dynamodb.table-name}")
    private String TABLE_NAME;
    private final String PK_USER_PREFIX = "USER#";
    private final String SK_USER_PREFIX = "#USER#";
    private final String SK_TRANSACTION_PREFIX = "#TRANSACTION#";
    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private DynamoDBMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DynamoDBMapper(amazonDynamoDB);
    }

    public void testDatabase() {
        log.info("Testing Database...");
        findUser("bex");
        log.info("Database test complete");
    }

    public boolean createUser(User user) {
        return saveUser(user);
    }
    public boolean updateUser(User user) {
        return saveUser(user);
    }
    private boolean saveUser(User user) {
        WawsupTable table = WawsupTable.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imageUrl(user.getImageUrl())
                .email(user.getEmail())
                .language(user.getLanguage())
                .activated(true)
                .pk(PK_USER_PREFIX + user.getUserName())
                .sk(SK_USER_PREFIX + user.getUserName())
                .build();
        try {
            mapper.save(table);
            log.debug("[DynamoDao.saveUser] Writing new user to database: " + user.getUserName());
            return true;
        } catch (DynamoDBMappingException e) {
            log.error("[DynamoDao.saveUser] Mapping exception thrown while creating database user: " + e);
            return false;
        }
    }

    public boolean deleteUser(String userName) {
        HashMap<String,AttributeValue> key_to_get = new HashMap<String,AttributeValue>();
        key_to_get.put("PK", new AttributeValue(PK_USER_PREFIX + userName));
        key_to_get.put("SK", new AttributeValue(SK_USER_PREFIX + userName));

        try {
            amazonDynamoDB.deleteItem(TABLE_NAME, key_to_get);
            log.debug("[DynamoDao.deleteUser] User deleted: " + userName);
            return true;

        } catch (AmazonServiceException e) {
            log.debug("[DynamoDao.deleteUser] Could not delete: " + userName);
            return false;
        }
    }
    public Optional<User> getUser(String userName) {
        //TODO: Catch exceptions
        AttributeValue userPK = new AttributeValue(PK_USER_PREFIX + userName);
        AttributeValue userSK = new AttributeValue(SK_USER_PREFIX + userName);
        List<WawsupTable> results = mapper.query(WawsupTable.class,
                new DynamoDBQueryExpression<WawsupTable>()
                        .withConsistentRead(false)
                        .withKeyConditionExpression("PK = :v_pk and SK = :v_sk")
                        .withExpressionAttributeValues(Map.of(":v_pk", userPK,":v_sk", userSK )));
        if(results.size() > 0) {
            log.debug("[DynamoDao.getUser] User retrieved from database: " + userName);
            return Optional.of(WawsupTable.getUserFromTable(results.get(0)));
        } else {
            log.debug("[DynamoDao.getUser] User not found in database: " + userName);
            return Optional.empty();
        }
    }
    private int findUser(String userName) {
        AttributeValue userPK = new AttributeValue(PK_USER_PREFIX + userName);
        AttributeValue userSK = new AttributeValue(SK_USER_PREFIX + userName);
        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("PK = :v_pk and SK = :v_sk")
                .withExpressionAttributeValues(Map.of(":v_pk", userPK,":v_sk", userSK ));
        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        return -1;
    }

}
