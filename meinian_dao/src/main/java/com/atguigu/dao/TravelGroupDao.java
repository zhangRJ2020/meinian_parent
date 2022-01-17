package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {


    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(Map<String, Integer> paramData);

    Page findPage(@Param("queryString") String queryString);

    TravelGroup getById(Integer id);

    List<Integer> getTravelItemIdsByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteConnection(Integer travelGroupId);

    List<TravelGroup> findAll();

    void delete(Integer id);

    Long findItemCountByTravelGroupId(Integer id);

    Long findSetmeatCountByTravelGroupId(Integer id);

    List<TravelGroup> findTravelGroupById(Integer id);
}
