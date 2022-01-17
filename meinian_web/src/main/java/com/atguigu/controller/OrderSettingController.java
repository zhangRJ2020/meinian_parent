package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/7
 */

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping("/editNumberByOrderDate")
    public Result editNumberByOrderDate(@RequestBody Map map){
        try {
            orderSettingService.editNumberByOrderDate(map);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> maps = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS, maps);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/upload")
    //这里的这个形参名excelFile要和表单提交的name属性指定名字一致
    public Result upload(MultipartFile excelFile){

        try {
            //读取excel中的文件
            List<String[]> list = POIUtils.readExcel(excelFile);

            //封装实体类对象
            List<OrderSetting> listData = new ArrayList<>();
            for (String[] strings : list) {
                String dateStr = strings[0];
                String numberStr = strings[1];
                OrderSetting orderSetting = new OrderSetting();
                orderSetting.setOrderDate(new Date(dateStr));
                orderSetting.setNumber(Integer.parseInt(numberStr));
                //orderSetting.setReservations(0);
                listData.add(orderSetting);
            }

            //批量插头数据库中
            orderSettingService.addBatch(listData);

            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.UPLOAD_FAIL);
        }
    }

}
