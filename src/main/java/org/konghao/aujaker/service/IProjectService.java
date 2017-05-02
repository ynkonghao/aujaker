package org.konghao.aujaker.service;

/**
 * 初始化类
 * @author konghao
 *
 */
public interface IProjectService {
	public void initProject(String path);
	/**
	 * 打包package
	 * @param path
	 */
	public void mvnPackage(String path,String artifactId);
}
