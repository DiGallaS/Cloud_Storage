package ru.netology.cloudserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse implements Serializable {

    private static final long serialVersionUID = -8268626665487253090L;

    @JsonProperty("auth-token")
    private String authToken;
}
