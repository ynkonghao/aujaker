package org.konghao.aujaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.tools.NormalTools;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
