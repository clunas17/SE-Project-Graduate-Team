package com.example.controller;


import com.example.common.Result;
import com.example.common.enums.RecommendEnum;
import com.example.entity.Course;
import com.example.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @PostMapping("/add")
    public Result add(@RequestBody Course course) {

        courseService.add(course);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        courseService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        courseService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Course course) {
        courseService.updateById(course);
        return Result.success();
    }


    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Course course = courseService.selectById(id);
        return Result.success(course);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Course course ) {
        List<Course> list = courseService.selectAll(course);
        return Result.success(list);
    }



    @GetMapping("/selectPage")
    public Result selectPage(Course course,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Course> page = courseService.selectPage(course, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/getRecommend")
    public Result getRecommend(@RequestParam(value = "type", required = false) String type) {
        Course course = courseService.getRecommend(type);
        return Result.success(course);
    }

    @GetMapping("/selectTop8")
    public Result selectTop8(@RequestParam(value = "type", required = false) String type) {
        List<Course> list = courseService.selectTop8(type);
        return Result.success(list);
    }
    //added '(value = "type", required = false)' after '@RequestParam' to avoid error message 500
}