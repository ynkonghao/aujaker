package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;

public interface IControllerService {
	/**
	 * 在某个path文件夹中生成实体Controller信息
	 * @param path 生成的路径
	 * @param entitys 实体类列表
	 */
	public void generateControllers(String path,Map<String,Object> maps);
	/**
	 * 在path文件夹中生成一个Controller类
	 * @param path
	 * @param entity
	 */
	public void generateController(String path, ClassEntity entity, String artifactId, String groupId);
}
