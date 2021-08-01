package com.zmc.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.exceptions.MySQLDataException;
import com.zmc.health.dao.CheckGroupDao;
import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.CheckGroup;
import com.zmc.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/7/31
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */

    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先添加检查组
        checkGroupDao.add(checkGroup);
        //获取检查组的id、
        Integer checkGroupId = checkGroup.getId();
        //遍历选中的检查项ids数组
        if (null !=checkitemIds) {
            //添加检查组与检查项的关系
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroupId, checkitemId);
            }
        }
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<CheckGroup> checkGroupPage = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<CheckGroup>(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(int id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 通过id查询选中的检查项id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(int id) {

        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //更新检查组信息
        checkGroupDao.update(checkGroup);
        //删除旧关系
        checkGroupDao.deleteCheckGroupCheckItem(checkGroup.getId());
        //遍历添加新关系
        if (null !=checkitemIds) {
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addCheckGroupCheckItem(checkGroup.getId(),checkitemId);
            }
        }
    }

    /**
     * 通过id删除检查项
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) {
        //判断是否被套餐使用
        int count = checkGroupDao.findCountByCheckGroupId(id);
        //被使用了，报错
        if (count >0) {
            throw new MyException("该检查项被套餐使用了，不能删除");
        }
        //没使用
        //先删除关系，
        checkGroupDao.deleteCheckGroupCheckItem(id);
        //再删除检查组
        checkGroupDao.deleteById(id);
    }
}
