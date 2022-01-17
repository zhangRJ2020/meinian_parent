package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2021/12/31
 */

public interface TravelItemDao {

    void add(TravelItem travelItem);

    Page findPage(@Param("queryString") String queryString);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    Long findCountByTravelItemId(Integer id);

    List<TravelItem> findTravelItemById(Integer id);
}
