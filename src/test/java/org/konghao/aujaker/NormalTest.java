package org.konghao.aujaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.tools.NormalTools;
import org.konghao.aujaker.tools.RecordTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/5 16:06.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NormalTest {

    @Test
    public void test01() {
        String s = "a";
        System.out.println(NormalTools.isCract(s));
        System.out.println(NormalTools.isContainChinese(s));
        System.out.println(NormalTools.isContainChinese("adf.扎实.model"));
    }

    @Autowired
    private RecordTools recordTools;

    @Test
    public void test02() {
        recordTools.addRecord(RecordTools.XML_TYPE, "127.0.0.1");
        recordTools.addRecord(RecordTools.WEB_TYPE, "127.0.0.2");
        recordTools.addRecord(RecordTools.EXCEL_TYPE, "127.0.0.3");
    }

    @Test
    public void test03() {
        String str = recordTools.readRecord();
        System.out.println("=========="+str);
    }

    @Test
    public void test04() {
        File f = new File("D:/temp/upload");
        System.out.println(f.getName());
    }
}
