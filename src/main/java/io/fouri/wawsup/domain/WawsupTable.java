package io.fouri.wawsup.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@DynamoDBTable(tableName = "wawsup")
@NoArgsConstructor
public class WawsupTable {
    @DynamoDBAttribute(attributeName = "userName")
    private String userName;
    @DynamoDBAttribute(attributeName = "email")
    private String email;
    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;
    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;
    @DynamoDBAttribute(attributeName = "imageUrl")
    private String imageUrl;
    @DynamoDBAttribute(attributeName = "language")
    private String language;
    @DynamoDBAttribute(attributeName = "createDate")
    private String createDate;
    @DynamoDBAttribute(attributeName = "activated")
    private boolean activated;
    @DynamoDBAttribute(attributeName = "transactionType")
    private String transactionType;
    @DynamoDBAttribute(attributeName = "amount")
    private double amount;
    @DynamoDBAttribute(attributeName = "fromUser")
    private String fromUser;
    @DynamoDBAttribute(attributeName = "toUser")
    private String toUser;

    private String pk;
    private String sk;

    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return pk;
    }

    public void setPK(String pk) {
       this.pk = pk;
    }

    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return sk;
    }

    public void setSK(String sk) {
        this.sk = sk;
    }

    public static User getUserFromTable(WawsupTable table) {
        User user = User.builder()
                .userName(table.getUserName())
                .email(table.getEmail())
                .firstName(table.getFirstName())
                .lastName(table.getLastName())
                .imageUrl(table.getImageUrl())
                .language(table.getLanguage())
                .createDate(table.getCreateDate())
                .build();
        return user;
    }

}
