package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.CheckItem;
import com.zmc.health.service.CheckItemService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Package_Name: com.zmc.health.controller
 *
 * @author zmc
 * Date: 2021/7/29
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有检查项
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> list = checkItemService.findAll();
        //封装到result
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);

    }

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        //调用服务添加
        checkItemService.add(checkItem);
        //返回结果
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = (User) securityContext.getAuthentication().getPrincipal();
        System.out.println("登录的用户名=========" + user.getUsername());
        //调用服务查询
        PageResult<CheckItem> result = checkItemService.findPage(queryPageBean);
        //返回结果给页面
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,result);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    /**
     * 更新检查项
     * @param checkItem
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        //调用服务修改
        checkItemService.update(checkItem);
        //返回结果
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
       checkItemService.deleteById(id);
       return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
