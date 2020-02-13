package com.example.game.service;

import com.example.game.dto.request.LoginRequestObject;
import com.example.game.dto.request.SignUpRequestObject;
import com.example.game.dto.response.CommonResponse;
import com.example.game.mapper.UserMapper;
import com.example.game.model.User;
import com.example.game.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommonResponse login(HttpServletResponse response, LoginRequestObject requestObject) {
        String username = requestObject.getUsername();
        String password = requestObject.getPassword();
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            return new CommonResponse("错误的用户名或密码", 1000);
        }

        if (!user.getPassword().equals(password)) {
            return new CommonResponse("错误的用户名或密码", 1000);
        }

        String jwt = JwtUtil.createJwt(user);
        response.setHeader("Authorization", jwt);

        return new CommonResponse("ok", 200);
    }

    public CommonResponse signUp(HttpServletResponse response, SignUpRequestObject requestObject,
                                 boolean login) {
        String username = requestObject.getUsername();
        String password = requestObject.getPassword();
        String confirmPassword = requestObject.getPassword();

        System.out.println(username);
        System.out.println(password);
        System.out.println(confirmPassword);

        if (!password.equals(confirmPassword)) {
            return new CommonResponse("密码与确认密码不一致", 1000);
        }

        if (password.equals("")) {
            return new CommonResponse("密码不能为空", 1000);
        }

        if (username.equals("")) {
            return new CommonResponse("用户名不能为空", 1000);
        }

        // duplicate
        if (userMapper.selectUserByUsername(username) != null) {
            return new CommonResponse("用户名已存在", 1000);
        }

        try {
            userMapper.insertUser(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResponse("数据库错误", 1000);
        }

        if (login) {
            User user = userMapper.selectUserByUsername(username);
            String jwt = JwtUtil.createJwt(user);
            response.setHeader("Authorization", jwt);
        }

        return new CommonResponse("ok", 200);
    }
}
