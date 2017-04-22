package org.konghao.aujaker.service;

import java.util.Map;

import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.PropertiesBaseEntity;

/**
 * 实体类的基本Service，用来根据xml或者prop生成一组实体类
 * @author konghao
 *
 */
public interface IClassEntityService {
	
	/**
	 * 根据properties文件生成实体类
	 * @param file properties文件的路径
	 * 返回一个map，map中存储了artifactId等信息
	 */
	public Map<String,Object> generateModelsByProperties(String file);
	
	public Map<String,Object> generateModelsByProperties();
	/**
	 * 根据xml生成实体类
	 * @param path 生成的位置
	 * @param file xml的文件路径
	 */
	public Map<String,Object> generateModelsByXml(String file);
	
	public Map<String,Object> generateModelsByXml();
	/**
	 * 根据实体类获取主键属性
	 * @param ce
	 * @return
	 */
	public PropertiesBaseEntity getPrimaryProperties(ClassEntity ce);
}
