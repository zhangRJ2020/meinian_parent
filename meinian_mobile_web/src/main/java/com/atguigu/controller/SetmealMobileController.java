package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/8
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    //只查套餐对象，不查对应联系的跟团游和自由行
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){
        Setmeal setmeal = setmealService.getSetmealById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    //查询套餐对象，和对应联系的跟团游和自由行
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
    }

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> list = setmealService.getSetmeal();
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, list);
    }

}
