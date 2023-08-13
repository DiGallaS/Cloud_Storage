package ru.netology.cloudserver.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRequest implements Serializable {
    private static final long serialVersionUID = 1114233239413817135L;

    private String login;
    private String password;
}
