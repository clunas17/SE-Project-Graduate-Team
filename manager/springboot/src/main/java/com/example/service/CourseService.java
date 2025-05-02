package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RecommendEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Course;
import com.example.exception.CustomException;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;


    public void add(Course course) {
        if(ObjectUtil.isNotEmpty(course.getRecommend()) && RecommendEnum.YES.status.equals(course.getRecommend())){
            Course recommend = courseMapper.getRecommend(course.getType());
            if(ObjectUtil.isNotEmpty(recommend)){
                throw new CustomException(ResultCodeEnum.RECOMMEND_EXIST_ERROR);
            }
        }
        course.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        courseMapper.insert(course);
    }


    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            courseMapper.deleteById(id);
        }
    }

    public void updateById(Course course) {
        if(ObjectUtil.isNotEmpty(course.getRecommend()) && RecommendEnum.YES.status.equals(course.getRecommend())){
            Course recommend = courseMapper.getRecommend(course.getType());
            if(ObjectUtil.isNotEmpty(recommend) && !recommend.getId().equals(course.getId())){
                throw new CustomException(ResultCodeEnum.RECOMMEND_EXIST_ERROR);
            }
        }
        courseMapper.updateById(course);
    }


    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }


    public List<Course> selectAll(Course course) {
        return courseMapper.selectAll(course);
    }


    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll(course);
        return PageInfo.of(list);
    }

    public Course getRecommend(String type) {
        return courseMapper.getRecommend(type);
    }

    public List<Course> selectTop8(String type) {
        return courseMapper.selectTop8(type);
    }

}