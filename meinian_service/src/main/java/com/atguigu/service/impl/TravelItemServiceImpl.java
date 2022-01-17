package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2021/12/31
 */

@Service(interfaceClass = TravelItemService.class)    //发布服务，注册到zk
@Transactional //生命式事务，所有方法都增加事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    private TravelItemDao travelItemDao;

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public TravelItem findById(Integer id) {

        TravelItem travelItem = travelItemDao.findById(id);
        return travelItem;
    }

    @Override
    public void delete(Integer id) {
        //当前id为自由行id
        //查询关联表中是否存在关联数据，如果存在就抛异常，不删除

        Long count = travelItemDao.findCountByTravelItemId(id);
        if (count > 0){
            throw new RuntimeException("删除自由行失败，因为存在关联数据，请先解除关系再删除");
        }
        travelItemDao.delete(id);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //使用分页插件分页
        //开启分页功能
        //底层使用 ：limit a b  意思为从第a条数据开始，往后查询b条数据，包含第a条数据，不包含第a+b条数据
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelItemDao.findPage(queryString);//返回当前夜页数据
        //第一个参数是总记录数，第二个参数是分页数据集合
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }
}
