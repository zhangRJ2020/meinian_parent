package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/10
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findById")
    public Result findById(Integer id){

        Map<String, Object> map = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
    }

    //由于页面表单的数据来自多张表，没办法用实体类接参数，就用map接
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map){
        try {

            /*点击提交预约, 把用户信息 提交到服务器
                    在Controller里面
            - 获得用户信息
                    - 校验验证码(redis里面存的和用户输入的比较)
                    - 调用业务, 进行预约, 响应*/

            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");
            String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

            if (redisCode == null || !redisCode.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }

            Result result = orderService.saveOrder(map);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
    }
}
