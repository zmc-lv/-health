package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.Setmeal;
import com.zmc.health.service.SetmealService;
import com.zmc.health.utils.QiNiuUtils;
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/2
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;
    /**
     * 套餐图片上传
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //- 获取文件名，获取它的后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //- 产生唯一名称，拼接后缀名， 图片名
        String imgName = UUID.randomUUID().toString() + suffix;
        //- 调用QiNiuUtils上传
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),imgName);
            //- 成功后返回图片名和域名
            //{
            //    	imgName: 图片名
            //    	domain: 域名
            //	}
            Map<String,String> resultMap = new HashMap<String,String>();
            resultMap.put("imgName",imgName);
            resultMap.put("domain",QiNiuUtils.DOMAIN);

            return new Result(true, MessageConstant.UPLOAD_SUCCESS,resultMap);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("上传文件失败",e);
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        //调用服务添加
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     * @return
     */
    @PostMapping("findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        resultMap.put("domain",QiNiuUtils.DOMAIN);
        resultMap.put("setmeal",setmeal);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /**
     * 通过套餐id查询选中的检查组id集合
     * @param id
     * @return
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> ids = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS, ids);
    }

    /**
     * 更新套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        //调用服务更新
        setmealService.update(setmeal, checkgroupIds);
        return new Result(true,"更新套餐成功");
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用服务删除
        setmealService.deleteById(id);
        return new Result(true,"删除套餐成功");
    }

}


