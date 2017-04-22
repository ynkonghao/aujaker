package org.konghao.aujaker;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IClassEntityService;
import org.konghao.aujaker.service.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTests {
	
	@Autowired
	private IRepositoryService repoistoryService;
	@Autowired
	private IClassEntityService classEntityService;
	
	@Test
	public void testBaseUrl() {
		repoistoryService.copyBaseSrc("d:/test/aujaker/");
	}
	
	@Test
	public void testGenerateRepository() {
		Map<String,Object> maps = classEntityService.generateModelsByProperties();
		repoistoryService.generateRepository(maps, "d:/test/aujaker");
	}

}
