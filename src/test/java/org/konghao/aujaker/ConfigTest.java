package org.konghao.aujaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigTest {
	@Autowired
	private IConfigService configService;
	
	@Test
	public void testProperties() {
		configService.generateApplicatoinPropertiesByProp("d:/test/aujaker", "aujaker.properties");
	}
}
