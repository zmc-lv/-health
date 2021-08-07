package com.zmc.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.service.MemberService;
import com.zmc.health.service.ReportService;
import com.zmc.health.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/7
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 会员数量折线图
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        //先构建12个月
        List<String> months = new ArrayList<>(12);
        //日历对象
        Calendar car = Calendar.getInstance();
        //过去一年 减掉一年
        car.add(Calendar.YEAR,-1);
        //遍历12次
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            //每次加1,加12个月
            car.add(Calendar.MONTH, 1);
            Date date = car.getTime();
            String month = sd.format(date);
            months.add(month);
        }
        List<Integer> memberCount = memberService.getMemberReport(months);
        //封装到map
        HashMap<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);
        return  new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    /**
     * 套餐预约占比
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){

        List<Map<String,Object>> reportData= setmealService.getSetmealReport();
        //提取套餐的名称
        List<String> setmealNames = reportData.stream().map(map -> {
            return (String) map.get("name");
        }).collect(Collectors.toList());
        HashMap<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",reportData);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    /**
     * 运营数据统计
     * @return
     */
    @GetMapping("getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String, Object> resultData = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,resultData);
    }

    /**
     * 导出运营统计数据excel报表
     * @return
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //查询报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //获取模板文件
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        //创建工作簿，读取模板
        try {
            Workbook wk = new XSSFWorkbook(template);
            //获取工作表
            Sheet sht = wk.getSheetAt(0);
            //通过模板填充数据
            sht.getRow(2).getCell(5).setCellValue((String) reportData.get("reportDate"));
            //===================会员数量统计
            sht.getRow(4).getCell(5).setCellValue((Integer) reportData.get("todayNewMember"));
            sht.getRow(4).getCell(7).setCellValue((Integer) reportData.get("totalMember"));
            sht.getRow(5).getCell(5).setCellValue((Integer) reportData.get("thisWeekNewMember"));
            sht.getRow(5).getCell(7).setCellValue((Integer) reportData.get("thisMonthNewMember"));

            //=============预约到诊===========
            sht.getRow(7).getCell(5).setCellValue((Integer) reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer) reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer) reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer) reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer) reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer) reportData.get("thisMonthVisitsNumber"));

            //热门套餐
            List<Map> hotSetmeal = (List<Map>) reportData.get("hotSetmeal");
            int rowIndex = 12;
            for (Map map : hotSetmeal) {
                Row row = sht.getRow(rowIndex);
                row.getCell(4).setCellValue((String) map.get("name"));
                row.getCell(5).setCellValue((Long) map.get("setmeal_count"));
                BigDecimal bigDecimal = (BigDecimal) map.get("proportion");
                row.getCell(6).setCellValue(bigDecimal.doubleValue());
                row.getCell(7).setCellValue((String) map.get("remark"));
                rowIndex++;
            }
            //内容体设置 告诉浏览器，下载的文件2就是excel的文档
            response.setContentType("application/vnd.ms-excel");
            java.lang.String filename = "运营数据统计.xlsx";
            //原始数据字节流
            byte[] bytes = filename.getBytes();
            filename = new String(bytes, "ISO-8859-1");
            System.out.println("转成字符串" + filename);
            //响应头信息设置
            response.setHeader("Content-Disposition", "attachment;filename="+filename);
            //写到输出流
            wk.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

      //关闭工作簿
    }


    /**
     * 导出运营统计数据excel报表  升级版
     * @return
     */
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest request, HttpServletResponse response) {
        //查询报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //获取模板文件
        String template = request.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        //创建工作簿，读取模板
        // 数据模型
        Context context = new PoiContext();
        context.putVar("obj",reportData);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),response.getOutputStream(),context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
