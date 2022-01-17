package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/7
 */

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void editNumberByOrderDate(Map map) {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = formatter.parse(String.valueOf(map.get("orderDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = orderSettingDao.findOrderSettingByOrderDate(date);
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setNumber(Integer.parseInt(String.valueOf(map.get("value"))));
        orderSetting.setOrderDate(date);
        if (count > 0){
            orderSettingDao.edit(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String startDate = date + "-1";
        String endDate = date + "-31";
        Map param = new HashMap();
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        List<OrderSetting> settingList = orderSettingDao.getOrderSettingByMonth(param);
        List<Map> maps = new ArrayList<>();
        for (OrderSetting orderSetting : settingList) {
            Map map = new HashMap();
            Date orderDate = orderSetting.getOrderDate();
            map.put("date", orderDate.getDate());
            map.put("number", orderSetting.getNumber());
            map.put("reservations", orderSetting.getReservations());
            maps.add(map);
        }
        return maps;
    }

    @Override
    public void addBatch(List<OrderSetting> listData) {
        for (OrderSetting orderSetting : listData) {
            //如果日期对应设置存在，就修改，否则就添加
            int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            if (count > 0){
                //预约设置存在了
                orderSettingDao.edit(orderSetting);
            }else {
                orderSettingDao.add(orderSetting);
            }
        }
    }
}
