package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {


    int insert(User user);

    int deleteById(Integer id);

    
    int updateById(User user);

    
    User selectById(Integer id);

    
    List<User> selectAll(User user);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);


}
