package com.zmc.test;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 包名: com.zmc.test
 *
 * @author zmc
 * 日期: 2021/8/7
 */

public class ITextTest {

    @Test
    public void testTest() throws Exception {
        //创建一个文件
        Document doc = new Document();
        //设置一个文件的保存路径
        PdfWriter.getInstance(doc,new FileOutputStream(new File("d:\\itext.pdf")));
        //打开文档
        doc.open();
        // 添加段落
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //给文档添加内容
        doc.add(new Paragraph("你是我的小苹果 hh",new Font(bfChinese)));
        //关闭文档
        doc.close();
    }
}
