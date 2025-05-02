package com.example.controller;


import com.example.common.Result;
import com.example.entity.Information;
import com.example.service.InformationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;



@RestController
@RequestMapping("/information")
public class InformationController {

    @Resource
    private InformationService informationService;


    @PostMapping("/add")
    public Result add(@RequestBody Information information) {
        informationService.add(information);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        informationService.deleteById(id);
        return Result.success();
    }


    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        informationService.deleteBatch(ids);
        return Result.success();
    }


    @PutMapping("/update")
    public Result updateById(@RequestBody Information information) {

        informationService.updateById(information);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Information information = informationService.selectById(id);
        return Result.success(information);
    }

    @GetMapping("/selectAll")
    public Result selectAll(Information information ) {
        List<Information> list = informationService.selectAll(information);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Information information,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Information> page = informationService.selectPage(information, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/getRecommend")
    public Result getRecommend() {
        Information information = informationService.getRecommend();
        return Result.success(information);
    }

    @GetMapping("/selectTop8")
    public Result selectTop8() {
        List<Information> list = informationService.selectTop8();
        return Result.success(list);
    }

}
