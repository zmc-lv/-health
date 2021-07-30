package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.CheckItemDao;
import com.zmc.health.pojo.CheckItem;
import com.zmc.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Package_Name: com.zmc.health.service.impl
 *
 * @author zmc
 * Date: 2021/7/29
 */
@Service(interfaceClass =CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
