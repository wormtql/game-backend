package com.example.game.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestObject {
    private String username;
    private String password;
    private String confirmPassword;
}
