package org.konghao.aujaker;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}
