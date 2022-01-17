package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/6
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelgroupIds, @RequestBody Setmeal setmeal){

        try {
            setmealService.add(travelgroupIds, setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {//不加注解要和表单提交的name一致才可以接收数据

        try {
            //1、获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();

            //2、生成新的文件名称（防止上传同名文件被覆盖）
            //gxt17.jpg
            int index = originalFilename.lastIndexOf(".");
            //.jpg
            String suffix = originalFilename.substring(index);
            String fileName = UUID.randomUUID().toString() + suffix;

            //3、调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            //=====================补充代码，定时清除七牛云上垃圾图片================
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);

            //4、返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
