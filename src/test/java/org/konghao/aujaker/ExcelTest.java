package org.konghao.aujaker;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.konghao.aujaker.service.IExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelTest {

	@Autowired
	private IExcelService excelService;
	
	@Test
	public void testExcel() {
		Map<String,Object> maps = excelService.xlsToEntity("test.xlsx");
		for(String key:maps.keySet()) {
			System.out.println(key+":"+maps.get(key));
		}
		List<ClassEntity> ces = (List<ClassEntity>)maps.get(FinalValue.ENTITY);
		for(ClassEntity ce:ces) {
			System.out.println(ce);
			for(PropertiesBaseEntity pbe:ce.getProps()) {
				System.out.println(pbe);
			}
		}
	}
	
	@Test
	public void testExcelTest() {
		excelService.generateImpotTest("d:", "test.xlsx");
	}
}
