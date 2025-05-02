package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RecommendEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.StatusEnum;
import com.example.entity.Information;
import com.example.exception.CustomException;
import com.example.mapper.InformationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class InformationService {

    @Resource
    private InformationMapper informationMapper;


    public void add(Information information) {
        information.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        informationMapper.insert(information);
    }


    public void deleteById(Integer id) {
        informationMapper.deleteById(id);
    }


    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            informationMapper.deleteById(id);
        }
    }


    public void updateById(Information information) {
        if (StatusEnum.NO.status.equals(information.getStatus())) {
            information.setRecommend(RecommendEnum.NO.status);
        }
        if (RecommendEnum.YES.status.equals(information.getRecommend())) {
            Information dbInformation = informationMapper.selectRecommend();
            if (ObjectUtil.isNotEmpty(dbInformation) && !dbInformation.getId().equals(information.getId())) {
                throw new CustomException(ResultCodeEnum.RECOMMEND_EXIST_ERROR);
            }
        }

        informationMapper.updateById(information);
    }


    public Information selectById(Integer id) {
        return informationMapper.selectById(id);
    }

    public List<Information> selectAll(Information information) {
        return informationMapper.selectAll(information);
    }


    public PageInfo<Information> selectPage(Information information, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Information> list = informationMapper.selectAll(information);
        return PageInfo.of(list);
    }

    public Information getRecommend() {
        return informationMapper.selectRecommend();
    }

    public List<Information> selectTop8() {
        return informationMapper.selectTop8();
    }

}