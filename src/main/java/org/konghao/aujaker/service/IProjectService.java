package org.konghao.aujaker.service;

/**
 * 初始化类
 * @author konghao
 *
 */
public interface IProjectService {
	public void initProject(String path);
	/**
	 * 通过maven对项目进行打包package
	 * @param path
	 */
	public void mvnPackage(String path,String artifactId);
	/**
	 * 生成最后的包，两个文件夹，一个是src用来存储源码，使用xxx.jar用来直接运行，两个包都最后都放到artifactId的包中
	 * 操作流程，先将target文件中的xxx.jar文件拷贝出来，之后删除target文件夹，之后将文件拷贝到src目录中，然后进行打包
	 * @param path
	 */
	public void generateReleasePackage(String path,String artifactId);
}
