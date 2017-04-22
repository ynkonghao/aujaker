package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;

/**
 * 生成Repository层的内容，首先需要拷贝个人开发的BaseRepository工厂和Specification
 * @author konghao
 *
 */
public interface IRepositoryService {

	/**
	 * 将baseSrc中的路径拷贝到路径中
	 */
	public void copyBaseSrc(String path);
	/**
	 * 根据实体类生成每个实体类的接口
	 * @param entities
	 */
	public void generateRepository(Map<String,Object> maps,String path);
	
	public void generateRepository(ClassEntity entity,String path,String artifactId);
}
