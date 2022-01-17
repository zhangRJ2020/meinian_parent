package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/6
 */

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Map<String, Object>> findSetmealCountReport() {

        return setmealDao.findSetmealCountReport();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        //1、保存套餐
        setmealDao.add(setmeal);//这里要完成一个主键回显操作
        //2、保存关联数据
        Integer setmealId = setmeal.getId();
        setSetmealAndTravleGroup(travelgroupIds, setmealId);

        //===========保存图片名到redis，这些图片名是放入mysql中的===================
        savePic2Redis(setmeal.getImg());
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }


    private void setSetmealAndTravleGroup(Integer[] travelgroupIds, Integer setmealId) {
        if (travelgroupIds != null && travelgroupIds.length > 0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String, Integer> paramMap = new HashMap<>();
                paramMap.put("travelgroupId", travelgroupId);
                paramMap.put("setmealId", setmealId);
                setmealDao.addSetmealAndTravleGroup(paramMap);
            }
        }
    }

}
