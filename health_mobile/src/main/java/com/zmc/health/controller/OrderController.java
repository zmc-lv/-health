package com.zmc.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.constant.RedisMessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.CheckGroup;
import com.zmc.health.pojo.CheckItem;
import com.zmc.health.pojo.Order;
import com.zmc.health.pojo.Setmeal;
import com.zmc.health.service.OrderService;
import com.zmc.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/5
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    @Reference
    private OrderService orderService;
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> parMap){
        //      验证码校验
        Jedis jedis = jedisPool.getResource();
        // 获取手机号码
        String telephone = parMap.get("telephone");
        // 拼接redis的key
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        //  再从redis取出验证码
        String codeInRedis = jedis.get(key);
        //  设值，用户可能没有点击获取验证，验证码过程，返回 重新获取验证
        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新获取验证码");
        }
        // 校验前端提交的验证码与redis中的验证码是否一致
        if (!codeInRedis.equals(parMap.get("validateCode"))) {
            //  不一致，返回，验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //  一致，删除验证码的key
        jedis.del(key);
        jedis.close();
        //设置预约类型,health_mobile微信预约
        parMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用服务进行预约
        Integer id = orderService.submit(parMap);
        return new Result(true,MessageConstant.ORDER_SUCCESS,id);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Map<String,Object> orderInfo = orderService.findOrderDetailById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }

    /**
     * 导出pdf
     * @param id
     */
    @GetMapping("/exportPdf")
    public void exportPdf(int id, HttpServletResponse res) throws Exception {
        //查询订单信息orderInfo，orderDao映射文件，覅ndByIdDetail要查询套餐id
        Map<String, Object> orderInfo = orderService.findOrderDetailById(id);
        //  获取套餐id
        Integer setmealId = (Integer) orderInfo.get("setmeal_id");
        //  通过套餐id获得套餐详情
        Setmeal setmealDetail = setmealService.findDetailById(setmealId);
        //  生成pdf
        //  创建Docuemnt
        Document doc = new Document();
        //  内容体格式
        res.setContentType("application/pdf");
        //  响应头信息
        res.setHeader("Context-Disposition","attachment;filename=orderInfo.pdf");
        //  定义输出PDFwriter
        PdfWriter.getInstance(doc,res.getOutputStream());
        //  打开open
        doc.open();
        //  添加内容
        Font chinese =new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
        //  订单信息
        doc.add(new Paragraph("体检人"+ orderInfo.get("member"),chinese));
        doc.add(new Paragraph("体检套餐"+ orderInfo.get("setmeal"),chinese));
        doc.add(new Paragraph("体检日期"+ orderInfo.get("orderDate"),chinese));
        doc.add(new Paragraph("预约类型"+ orderInfo.get("orderType"),chinese));
        //  套餐详情 table 遍历套餐下的检查组，检查组下的检查项
        //构建一个3列的表格，行是不固定的，给表格添加单元格，每超过都是三个，都自动换行
        Table table = new Table(3);

        // ==============表格样式==============
        table.setWidth(80); // 宽度
        table.setBorder(1); // 边框
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
        /*设置表格属性*/
        table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
        table.setPadding(5);//设置表格与字体间的间距
//table.setSpacing(5);//设置表格上下的间距
        table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式
        // ==============表格样式==============


        table.addCell(buildCell("项目名称",chinese));
        table.addCell(buildCell("项目内容",chinese));
        table.addCell(buildCell("项目解读",chinese));

        //检查组与检查项
        List<CheckGroup> checkGroups = setmealDetail.getCheckGroups();
        for (CheckGroup checkGroup : checkGroups) {
            //检查组名称
            table.addCell(buildCell(checkGroup.getName(),chinese));
            //检查项
            List<CheckItem> checkItems = checkGroup.getCheckItems();
            StringJoiner joiner = new StringJoiner("");
            for (CheckItem checkItem : checkItems) {
                joiner.add(checkItem.getName());
            }
            table.addCell(buildCell(joiner.toString(),chinese));
            //检查项remark
            table.addCell(buildCell(checkGroup.getRemark(),chinese));
        }
        doc.add(table);
        //  解决中文显示问题
        //  关闭文档
        doc.close();
    }

    //传递内容和字体样式，生成单元格
    private Cell buildCell(String content,Font font) throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);
    }
}
