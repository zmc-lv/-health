package com.zmc.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.pojo.Setmeal;
import com.zmc.health.service.SetmealService;
import com.zmc.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/26
 */
@Component
public class GenerateHtmlJob {

    private static final Logger log = LoggerFactory.getLogger(GenerateHtmlJob.class);

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Reference
    private SetmealService setmealService;

    /**
     * PostConstruct 初始化方法
     */
    @PostConstruct
    public void init(){
        // 设置模板路径
        freemarkerConfiguration.setClassForTemplateLoading(GenerateHtmlJob.class,"/ftl");
        // 设置编码
        freemarkerConfiguration.setDefaultEncoding("utf-8");
    }

    @Value("${out_put_path}")
    private String out_put_path; // 存放静态文件的目录

    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void generateHtml(){
        log.info("generateHtml 任务启动了.......");
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Set<String> ids = jedis.smembers(key);
        log.debug("要处理的套餐id 个数:{}", ids.size());
        ids.forEach(id -> {
            //id  id|操作类型|时间戳
            String[] idInfo = id.split("\\|");
            Integer setmealId = Integer.valueOf(idInfo[0]); // 套餐id
            // 操作类型
            String operationType = idInfo[1];
            // setmeal_{id}.html, 存放的文件完整路径
            String filename = out_put_path + File.separator + "setmeal_" + setmealId + ".html";
            if("1".equals(operationType)){
                generateDetailHtml(setmealId,filename);
            }else if ("0".equals(operationType)){
                log.debug("删除静态页面 {}", setmealId);
                new File(filename).delete();
            }
            // 删除redis中对应的id值
            jedis.srem(key,id);
        });

        if(ids.size() > 0){
            // 生成列表页面
            generateSetmealList();
        }
        jedis.close();
    }

    private void generateSetmealList(){
        log.debug("生成列表页面");
        String templateName = "mobile_setmeal.ftl";
        // 构建详情数据 Map<String,Object>
        List<Setmeal> list = setmealService.findAll();
        // 拼接图片路径
        list.forEach(setmeal ->{setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());});
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList",list);
        // 存放的目录文件全路径
        String filename = out_put_path + File.separator + "mobile_setmeal.html";
        generateStaticHtml(filename, templateName, dataMap);
        log.info("生成套餐列表静态页面成功");
    }

    /**
     * 生成 详情页面
     * @param setmealId
     * @param filename
     */
    private void generateDetailHtml(Integer setmealId, String filename){
        log.debug("生成详情静态页面 {}", setmealId);
        String templateName = "mobile_setmeal_detail.ftl";
        // 构建详情数据 Map<String,Object>
        Setmeal setmeal = setmealService.findDetailById(setmealId);
        // 拼接图片路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmeal",setmeal);
        generateStaticHtml(filename, templateName, dataMap);
        log.info("生成套餐详情静态页面成功");
    }

    /**
     * 生成 页面
     * @param filename
     * @param templateName
     * @param dataMap
     */
    private void generateStaticHtml(String filename,String templateName, Map<String,Object> dataMap){
        try {
            //获取模板对象
            Template template = freemarkerConfiguration.getTemplate(templateName);
            // utf-8 不能少了。少了就中文乱码
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            // 给模板填充数据
            template.process(dataMap,writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("生成静态页面失败 {}",filename, e);
        }
    }
}
