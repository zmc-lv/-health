package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.MemberDao;
import com.zmc.health.dao.OrderDao;
import com.zmc.health.service.ReportService;
import com.zmc.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/8/7
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;
    /**
     * 运营数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String todayStr = sdf.format(today);
        //reportDate
        String reportDate = todayStr;
        // 星期一
        String monday = sdf.format(DateUtils.getThisWeekMonday());
        // 周天
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        // ================== 会员数量统计 ====================
        //todayNewMember
        Integer todayNewMember = memberDao.findMemberCountByDate(todayStr);
        //totalMember
        Integer totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        // ==================== 预约数量统计 ==================
        //todayOrderNumber
        Integer todayOrderNumber = orderDao.findOrderCountByDate(todayStr);
        //todayVisitsNumber
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(todayStr);
        //thisWeekOrderNumber
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //thisWeekVisitsNumber
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth,lastDayOfThisMonth);
        //thisMonthVisitsNumber
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //hotSetmeal
        List<Map> hotSetmeal = orderDao.findHotSetmeal();
        Map<String,Object> reportData = new HashMap<String,Object>(12);
        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }
}
