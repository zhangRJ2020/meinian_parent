package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetmealDao {


    void add(Setmeal setmeal);

    void addSetmealAndTravleGroup(Map<String, Integer> paramMap);

    Page findPage(@Param("queryString") String queryString);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map<String, Object>> findSetmealCountReport();

}
