package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.OrderSetting;
import com.zmc.health.service.OrderSettingService;
import com.zmc.health.utils.POIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/3
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;
    public static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);


    /**
     * 批量导入预约设置
     * @param excelFile
     * @return
     */
   @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
       //接收上传的文件,MultipartFile 参数为excelFile
       //调用poiutils解析excel,List<String[]>
       try {
           List<String[]> orderInfoStringArrList = POIUtils.readExcel(excelFile);
           final SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
          List<OrderSetting> orderSettingList =  orderInfoStringArrList.stream().map(orderInfoStringArr ->{
               OrderSetting os = new OrderSetting();
               String orderDateStr = orderInfoStringArr[0];//日期的字符串
              try {
                  os.setOrderDate(sdf.parse(orderDateStr));
              } catch (ParseException e) {
              }
              os.setNumber(Integer.valueOf(orderInfoStringArr[1]));//最大预约数量
              return os;
           }).collect(Collectors.toList());
           //调用服务导入
           orderSettingService.add(orderSettingList);
       }catch (Exception e) {
           log.error("导入预约设置失败",e);
           return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
       }
       //返回操作的结果给页面
       return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
   }

    /**
     * 日历展示
     * @param month
     * @return
     */
   @GetMapping("/getDataByMonth")
    public Result getDataByMoth(String month){
       //调用服务查询
       List<Map<String,Integer>> monthData = orderSettingService.getDataByMoth(month);
       return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,monthData);
   }

    /**
     * 基于日历的预约设置
     * @return
     */
   @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting os){
       //调用服务来跟新
       orderSettingService.editNumberByDate(os);
       return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
   }
}
