package com.zmc.health.service;

import java.util.Map;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/7
 */
public interface ReportService {
    /**
     * 运营数据统计
     * @return
     */
    Map<String, Object> getBusinessReportData();

}
