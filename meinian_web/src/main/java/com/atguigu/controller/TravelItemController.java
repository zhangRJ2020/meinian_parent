package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2021/12/31
 */

@RestController
@RequestMapping("/travelItem")
public class TravelItemController {

    @Reference
    private TravelItemService travelItemService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TravelItem> list = travelItemService.findAll();
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('TRAVELITEM_EDIT')")//权限校验
    public Result edit(@RequestBody TravelItem travelItem){
        try {
            travelItemService.edit(travelItem);
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id){
        try {
            TravelItem travelItem = travelItemService.findById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('TRAVELITEM_DELETE')")//权限校验，使用TRAVELITEM_DELETE123测试
    public Result delete(@RequestParam("id") Integer id){//@RequestBody作用是从请求体中获取数据
        try {
            travelItemService.delete(id);
            return new Result(true, MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){//@RequestBody作用是从请求体中获取数据
        return travelItemService.findPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
    }

    //表单项参数名称要和实体类的属性名称一致，提供对应的set方法，框架会创建对象并封装数据
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")//权限校验
    public Result add(@RequestBody TravelItem travelItem){//@RequestBody作用是从请求体中获取数据

        //ctrl+alt + t 是try-catch快捷键
        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }

    }
}
