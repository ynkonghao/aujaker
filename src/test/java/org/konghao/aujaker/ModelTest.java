package org.konghao.aujaker;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IClassEntityService;
import org.konghao.aujaker.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelTest {
	
	@Autowired
	private IModelService modelService;
	@Autowired
	private IClassEntityService classEntityService;
	
	@Test
	public void testGenerateProp() {
		Map<String,Object> maps = classEntityService.generateModelsByProperties();
		modelService.generateModels("d:/test/aujaker", maps);
	}
	
	@Test
	public void testGenerateXml() {
		Map<String,Object> maps = classEntityService.generateModelsByXml();
		modelService.generateModels("d:/test/aujaker", maps);
	}

}
