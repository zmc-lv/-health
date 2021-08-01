package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.CheckGroup;
import com.zmc.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/7/31
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        //调用服务添加检查组
        checkGroupService.add(checkGroup,checkitemIds);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务查询
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findByIds(int id){
       CheckGroup checkGroup =  checkGroupService.findById(id);
       return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkGroup);
    }

    /**
     * 通过id查询选中的检查项id集合
     * @return
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
    }


    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        //调用服务添加检查组
        checkGroupService.update(checkGroup,checkitemIds);
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 通过id删除检查项
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用服务删除
        checkGroupService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
