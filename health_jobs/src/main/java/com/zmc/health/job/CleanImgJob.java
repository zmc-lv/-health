package com.zmc.health.job;

import com.alibaba.dubbo.config.annotation.Reference;

import com.zmc.health.service.SetmealService;
import com.zmc.health.utils.QiNiuUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 包名: com.zmc.health.job
 *
 * 清理七牛上的辣鸡图片
 * @author zmc
 * 日期: 2021/8/3
 */
@Component
public class CleanImgJob {

    private static final Logger logger = LoggerFactory.getLogger(CleanImgJob.class);

    @Reference
    private SetmealService setmealService;
    //发布时应该  @Scheduled(cron = "0 0 2 * * ? *")
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void cleanImgJob() {
       logger.info("清理任务开始执行");
        List<String> img7Niu = QiNiuUtils.listFile();
        logger.debug("7牛上有{}张图片",null ==img7Niu?0:img7Niu.size());
        List<String> imgInDB = setmealService.findImgs();
        logger.debug("数据库有{}张图片",null ==imgInDB?0:imgInDB.size());
        //执行removeAll之后,img7Niu剩下的就是七牛上有的但数据库中没有的
        img7Niu.removeAll(imgInDB);
        logger.debug("要删除的图片有{}张",img7Niu.size());
        String[] need2Delete = img7Niu.toArray(new String[]{});
        QiNiuUtils.removeFiles(need2Delete);

        logger.info("删除{}张图片成功",img7Niu.size());

    }



}
