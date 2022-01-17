package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/4
 */

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    private TravelGroupDao travelGroupDao;

    @Override
    public void delete(Integer id) {
        //查询自由行和跟团游关联表
        Long count1 = travelGroupDao.findItemCountByTravelGroupId(id);

        //查询套餐游和自由行关联表
        Long count2 = travelGroupDao.findSetmeatCountByTravelGroupId(id);
        if(count1 > 0 || count2 > 0){
            throw new RuntimeException("删除自跟团游失败，因为存在关联数据，请先解除关系再删除");
        }
        travelGroupDao.delete(id);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        //先删除中间表的关联数据
        travelGroupDao.deleteConnection(travelGroupId);
        //重新增加关联数据
        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);
    }

    @Override
    public List<Integer> getTravelItemIdsByTravelGroupId(Integer id) {
        return travelGroupDao.getTravelItemIdsByTravelGroupId(id);
    }

    @Override
    public TravelGroup getById(Integer id) {
        return travelGroupDao.getById(id);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());//数据封装
    }

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);
        //下面这样是拿不到id的，id是自增长，所以必须进行主键回填,在xml配置文件中设置
        Integer travelGroupId = travelGroup.getId();
        setTravelGroupAndTravelItem(travelGroupId, travelItemIds);

    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds){
        if (travelItemIds != null && travelItemIds.length > 0){
            //准备dao层需要的参数，利用map集合作为参数传递

            for (Integer travelItemId : travelItemIds) {
                Map<String, Integer> paramData = new HashMap<>();
                paramData.put("travelGroupId", travelGroupId);
                paramData.put("travelItemId", travelItemId);
                travelGroupDao.addTravelGroupAndTravelItem(paramData);
            }
        }
    }
}
