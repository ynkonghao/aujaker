package org.konghao.aujaker;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("zsl")
public class ProjectServiceTest {
	@Autowired
	private IProjectService projectService;
	
	@Test
	public void testInit() {
		projectService.initProject("d:/test/mp");
	}
	
	@Test
	public void testPackage() {
		projectService.mvnPackage("d:/test/mp","helloAujaker");
	}
	
	@Test
	public void testTar() {
		projectService.generateReleasePackage("d:/test/mp", "helloAujaker");
	}

	@Test
	public void testInit2() {
		projectService.initProject("D:\\temp\\upload\\aujacker\\item", "D:\\temp\\upload\\aujacker\\9287c89c-801a-4dfb-9884-e7bea6574666.xml");
	}
	
	@Test
	public void testXls() {
		projectService.initProjectByXls("d:/test/xls", "test.xlsx");
	}
}
