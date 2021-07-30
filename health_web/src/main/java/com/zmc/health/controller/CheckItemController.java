package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.CheckItem;
import com.zmc.health.service.CheckItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
