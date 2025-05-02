package com.example.mapper;

import com.example.entity.Information;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface InformationMapper {

    int insert(Information information);

    int deleteById(Integer id);

    int updateById(Information information);

    Information selectById(Integer id);

    List<Information> selectAll(Information information);


    @Select("select * from information where recommend = 'Yes' and status = 'Approved'")
    Information selectRecommend();

    @Select("select * from information where recommend = 'No' and status = 'Approved' order by id limit 4")
    List<Information> selectTop8();
}