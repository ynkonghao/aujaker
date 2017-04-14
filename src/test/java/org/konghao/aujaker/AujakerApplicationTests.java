package org.konghao.aujaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.konghao.aujaker.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AujakerApplicationTests {
	
	@Autowired
	private IModelService modelService;
	
	@Test
	public void testRead() {
		Properties properties = System.getProperties(); 
		System.out.println(properties.get("user.name"));
/*		Iterator it =  properties.entrySet().iterator();  
		while(it.hasNext())  
		{  
		    Entry entry = (Entry)it.next();  
		    System.out.print(entry.getKey()+"=");  
		    System.out.println(entry.getValue());  
		}  
*/	}
	
	@Test
	public void contextLoads() {
		ClassEntity entity = new ClassEntity();
		entity.setClassName("Student2");
		entity.setPkgName("org.konghao.model");
		entity.setTableName("t_stu2");
		entity.setCommet("学生测试对象");
		List<PropertiesBaseEntity> props = new ArrayList<PropertiesBaseEntity>();
		PropertiesBaseEntity pbe = new PropertiesBaseEntity();
		pbe.setCommet("学生主键");
		pbe.setName("id");
		pbe.setPk(true);
		pbe.setPkType(1);
		pbe.setType("String");
		props.add(pbe);
		
		pbe = new PropertiesBaseEntity();
		pbe.setCommet("姓名");
		pbe.setName("name");
		pbe.setPk(false);
		pbe.setType("String");
		props.add(pbe);
		
		pbe = new PropertiesBaseEntity();
		pbe.setCommet("出生日期");
		pbe.setName("bornDate");
		pbe.setColumnName("born_date");
		pbe.setType("java.util.Date");
		props.add(pbe);
		
		pbe = new PropertiesBaseEntity();
		pbe.setCommet("个人介绍");
		pbe.setName("intro");
		pbe.setType("String");
		pbe.setLob(true);
		props.add(pbe);
		
		entity.setProps(props);
		
		modelService.generateModel("d:/test/", entity);
	}
	
	@Test
	public void testFile() {
		modelService.generateModelsByProperties("d:/test/aujaker", "aujaker.properties");
	}
	
	@Test
	public void testXml() {
		modelService.generateModelsByXml("d:/test/aujaker/xml", "stu.xml");
	}

}
