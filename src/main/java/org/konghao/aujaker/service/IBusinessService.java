package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;

public interface IBusinessService {
	public void generateService(Map<String,Object> maps,String path);
	
	public void generateService(ClassEntity entity, String path,String artifactId);
}
