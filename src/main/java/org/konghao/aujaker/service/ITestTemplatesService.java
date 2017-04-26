package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;

/**
 * 增加测试模板，仅仅只是增加一个标准的类
 * @author konghao
 *
 */
public interface ITestTemplatesService {
	public static final String SERVICE_TYPE = "Service";
	public static final String REPOS_TYPE = "Repoistory";
	/**
	 * 生成一个测试模板
	 * @param path
	 * @param entity
	 * @param artifactId
	 * @param type 是service或者repository
	 */
	public void generateTestTemplate(String path,ClassEntity entity,String artifactId,String type);
	
	public void generateTestTemplate(String path,Map<String,Object> maps,String type);
	
}
