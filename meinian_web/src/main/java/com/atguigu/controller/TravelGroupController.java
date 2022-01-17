package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/4
 */

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {

    @Reference
    private TravelGroupService travelGroupService;

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            travelGroupService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<TravelGroup> list = travelGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS, list);
    }

    @RequestMapping("/edit")
    //需要接收两部分数据
    public Result edit(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup){//@RequestBody作用是从请求体中获取数据

        //ctrl+alt + t 是try-catch快捷键
        try {
            travelGroupService.edit(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("getTravelItemIdsByTravelGroupId")
    public Result getTravelItemIdsByTravelGroupId(Integer travelGroupId){
        try {
            List<Integer> list = travelGroupService.getTravelItemIdsByTravelGroupId(travelGroupId);
            return new Result(true, "根据跟团游查询自由行成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "根据跟团游查询自由行失败");
        }
    }

    @RequestMapping("/getById")
    public Result getById(Integer id){
        try {
            TravelGroup travelGroup = travelGroupService.getById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS, travelGroup);//回显表单数据
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return travelGroupService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
    }

    @RequestMapping("/add")
    //需要接收两部分数据
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup){//@RequestBody作用是从请求体中获取数据

        //ctrl+alt + t 是try-catch快捷键
        try {
            travelGroupService.add(travelItemIds, travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }

}
