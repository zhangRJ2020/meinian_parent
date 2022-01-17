package com.atguigu.controller;

import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/10
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        try {
            //1、生成四位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            //2、发送验证码到手机
            SMSUtils.sendShortMessage(telephone, String.valueOf(code.toString()));

            //3、将验证码存储到redis中，进行后期验证
            //验证码5分钟有效
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5*60, String.valueOf(code));

            System.out.println("telephone="+telephone+" code="+code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try {
            //1、生成四位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);

            //2、发送验证码到手机
            SMSUtils.sendShortMessage(telephone, String.valueOf(code.toString()));

            //3、将验证码存储到redis中，进行后期验证
            //验证码5分钟有效
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5*60, String.valueOf(code));

            System.out.println("telephone="+telephone+" code="+code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

}
