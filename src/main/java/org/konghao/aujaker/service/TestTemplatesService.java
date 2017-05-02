package org.konghao.aujaker.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.springframework.stereotype.Service;

@Service
public class TestTemplatesService implements ITestTemplatesService {

	@Override
	public void generateTestTemplate(String path, ClassEntity entity, String artifactId, String type) {
		path = CommonKit.generateTestPath(path, artifactId, entity, type);
		FileWriter fw = null;
		try {
			fw = new FileWriter(path+"/Test"+entity.getClassName()+type+".java");
			StringBuffer sb = new StringBuffer();
			
			sb.append("package ").append(entity.getPkgName())
				.append(".").append(CommonKit.lowcaseFirst(type))
				.append(";\n\n");
			
			/*sb.append("import ").append(entity.getPkgName())
				.append(".").append("model.*;\n");
			sb.append("import ").append(entity.getPkgName())
				.append(".").append(CommonKit.lowcaseFirst(type))
				.append(".*;\n");*/
			
			sb.append("import org.junit.Test;\n");
			sb.append("import org.junit.runner.RunWith;\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.boot.test.context.SpringBootTest;\n");
			sb.append("import org.springframework.test.context.junit4.SpringRunner;\n\n");
			
			sb.append("@RunWith(SpringRunner.class)\n");
			sb.append("@SpringBootTest\n");
			sb.append("public class Test").append(entity.getClassName()).append(type)
				.append(" {\n\n");
			
			String oname = CommonKit.generateVarName(entity)+type;
			
			sb.append("\t@Autowired\n");
			sb.append("\tprivate I").append(entity.getClassName()).append(type)
				.append(" ").append(oname).append(";\n\n");
			sb.append("\t@Test\n");
			sb.append("\tpublic void test(){ \n");
			sb.append("\t}\n");
			
			sb.append("}\n");
			fw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw!=null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generateTestTemplate(String path, Map<String, Object> maps, String type) {
		List<ClassEntity> ces = (List<ClassEntity>)maps.get(FinalValue.ENTITY);
		for(ClassEntity ce:ces) {
			generateTestTemplate(path, ce, (String)maps.get(FinalValue.ARTIFACT_ID), type);
		}
	}

}
