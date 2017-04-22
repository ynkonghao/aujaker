package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;

/**
 * 生成model文件
 * @author konghao
 *
 */
public interface IModelService {
	/**
	 * 在某个path文件夹中生成实体信息
	 * @param path 生成的路径
	 * @param entitys 实体类列表
	 */
	public void generateModels(String path,Map<String,Object> maps);
	/**
	 * 在path文件夹中生成一个实体类
	 * @param path
	 * @param entity
	 */
	public void generateModel(String path,ClassEntity entity,String artifactId);
}
