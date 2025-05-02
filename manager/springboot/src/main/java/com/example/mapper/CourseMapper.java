package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CourseMapper {


    int insert(Course course);

    int deleteById(Integer id);

    int updateById(Course course);

    Course selectById(Integer id);

    List<Course> selectAll(Course course);


    @Select("select * from course where recommend = 'Yes' and type = #{type}")
    Course getRecommend(String type);

    @Select("select * from course where recommend = 'No' and type = #{type} order by id limit 4")
    List<Course> selectTop8(String type);


}