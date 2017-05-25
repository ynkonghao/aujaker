package org.konghao.aujaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 钟述林 393156105@qq.com on 2017/5/12 8:45.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class ZslExcelTest {

    @Autowired
    private IProjectService projectService;

    @Test
    public void testXls() {
        projectService.initProjectByXls("d:/test/xls", "testobj.xlsx", true);
    }

    @Test
    public void testXls2() {
        projectService.initProjectByXlsFile("d:/test/xls2", "D:/test/testobj.xlsx", true);
    }

    @Test
    public void testXls3() {
        projectService.initProjectByXlsFile("d:/test/xls3", "D:/test/testobj.xls", true);
    }

    @Test
    public void testXls4() {
        projectService.initProjectByXls("d:/test/xls4", "test1.xls", true);
    }
}
