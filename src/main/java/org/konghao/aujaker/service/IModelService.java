package org.konghao.aujaker.service;

import java.util.List;

import org.konghao.aujaker.model.ClassEntity;

/**
 * 生成model文件
 * @author konghao
 *
 */
public interface IModelService {
	public void generateModels(String path,List<ClassEntity> entitys);
	public void generateModel(String path,ClassEntity entity);
	public void generateModelsByProperties(String path,String file);
}
