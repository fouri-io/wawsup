package io.fouri.wawsup.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @JsonProperty("userName")
    private String userName; // Unique & used as id
    @JsonProperty("email")
    private String email;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("language")
    private String language;

    private String createDate;
    private boolean activated = false;
}
