package com.example.game.controller;

import com.example.game.dto.request.LoginRequestObject;
import com.example.game.dto.request.SignUpRequestObject;
import com.example.game.dto.response.CommonResponse;
import com.example.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @CrossOrigin(exposedHeaders = {"Authorization"})
    public CommonResponse login(@RequestBody LoginRequestObject requestObject, HttpServletResponse response) {
        return userService.login(response, requestObject);
    }

    @PostMapping("/sign-up")
    public CommonResponse signUp(@RequestBody SignUpRequestObject requestObject, HttpServletResponse response,
                                 @RequestParam(required = false, defaultValue = "false") boolean login) {
        return userService.signUp(response, requestObject, login);
    }
}
