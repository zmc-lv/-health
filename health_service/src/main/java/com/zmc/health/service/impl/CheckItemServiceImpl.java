package com.zmc.health.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.dao.CheckItemDao;
import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.exception.MyException;
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

    /**
     * 新增检查项
      * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //是否有查询条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //调用dao查询
        Page<CheckItem> checkItemPage = checkItemDao.findByCondition(queryPageBean.getQueryString());
        long total = checkItemPage.getTotal();//总记录数
        List<CheckItem> rows = checkItemPage.getResult();//分页结果集
        return new PageResult<>(total,rows);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //先判断这个检查项是否被检查组使用了
        //调用dao查询检查项的id是否在t_checkgroup_checkitem表中存在记录
        int cnt = checkItemDao.findCountByCheckItemId(id);
        //被使用了则不能删除
        if(cnt > 0){
            // 已经被检查组使用了，则不能删除，报错
            //??? health_web能捕获到这个异常吗？
            throw new MyException("该检查项被检查组使用了，不能删除");
        }
        //没使用就可以调用dao删除
        checkItemDao.deleteById(id);
    }


}
