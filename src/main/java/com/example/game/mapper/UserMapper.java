package com.example.game.mapper;

import com.example.game.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @Select("select * from user where name=${username}")
    User selectUserByUsername(String username);

    @Insert("insert into user (name, password) values ('${username}', '${password}')")
    void insertUser(String username, String password);
}
