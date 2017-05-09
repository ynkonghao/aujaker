package org.konghao.aujaker.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService implements IRepositoryService {
	
	@Autowired
	private IClassEntityService classEntityService;
	

	@SuppressWarnings("unchecked")
	@Override
	public void generateRepository(Map<String,Object> maps, String path) {
		String artifactId = (String)maps.get(FinalValue.ARTIFACT_ID);
		List<ClassEntity> ces = (List<ClassEntity>)maps.get(FinalValue.ENTITY);
		for(ClassEntity e:ces) {
			generateRepository(e, path,artifactId);
		}
	}
	

	@Override
	public void generateRepository(ClassEntity entity, String path,String artifactId) {
		path = CommonKit.generatePath(path, artifactId, entity,"repository");
		PropertiesBaseEntity pbe = classEntityService.getPrimaryProperties(entity);

		String fileName = "I"+entity.getClassName()+"Repository";
		StringBuffer sb = new StringBuffer();
		sb.append("package ").append(entity.getPkgName()).append(".repository;\n");
		sb.append("\n");
		if(pbe.getType().indexOf(".")>=0) {
			sb.append("import ").append(pbe.getType()).append(";\n");
		}
		String type = pbe.getType();
		if(pbe.getType().equals("int")) {
			type = "Integer";
		}
		sb.append("import ").append(entity.getPkgName()).append(".model.").append(entity.getClassName()).append(";\n");
		sb.append("import org.konghao.reposiotry.base.BaseRepository;\n");
		sb.append("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n");
		sb.append("\n");
		sb.append("public interface ").append(fileName)
		  .append(" extends BaseRepository<")
		  .append(entity.getClassName()).append(",")
		  .append(type).append(">,JpaSpecificationExecutor<")
		  .append(entity.getClassName()).append("> {\n");
		
		sb.append("\n");
		sb.append("}");
		FileWriter fw = null;
		try {
			fw = new FileWriter(path+"/"+fileName+".java");
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

}
