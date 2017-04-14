package org.konghao.aujaker.service;

import java.util.List;

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
	public void generateModels(String path,List<ClassEntity> entitys);
	/**
	 * 在path文件夹中生成一个实体类
	 * @param path
	 * @param entity
	 */
	public void generateModel(String path,ClassEntity entity);
	/**
	 * 根据properties文件生成实体类
	 * @param path 生成的位置
	 * @param file properties文件的路径
	 */
	public void generateModelsByProperties(String path,String file);
	/**
	 * 根据xml生成实体类
	 * @param path 生成的位置
	 * @param file xml的文件路径
	 */
	public void generateModelsByXml(String path,String file);
}
