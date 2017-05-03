package org.konghao.aujaker.service;

/**
 * 配置文件类，初始化基本的配置文件，初始化数据库的文件，application.properties
 * @author konghao
 *
 */
public interface IConfigService {
	
	public static final String MYSQL_DEP = "<dependency>\n"+
			"\t\t\t<groupId>mysql</groupId>\n"+
			"\t\t\t<artifactId>mysql-connector-java</artifactId>\n"+
			"\t\t</dependency>";
	
	public static final String SQLITE_DEP = "<dependency>\n"+
			"\t\t\t<groupId>org.xerial</groupId>\n"+
			"\t\t\t<artifactId>sqlite-jdbc</artifactId>\n"+
			"\t\t\t<version>3.7.2</version>\n"+
			"\t\t</dependency>";
	
	/**
	 * 根据prop文件写properties
	 * @param path
	 * @param propFile
	 */
	public void generateApplicatoinPropertiesByProp(String path,String propFile);
	/**
	 * 根据xml文件写properties
	 * @param path
	 * @param xmlFile
	 */
	public void generateApplicationPropertiesByXml(String path,String xmlFile);
	/**
	 * 根据properties写pom，文件写在src文件夹中
	 * @param path
	 * @param propFile
	 */
	public void generatePomByProp(String path,String propFile);
	/**
	 * 根据xml写pom
	 * @param path
	 * @param xmlFile
	 */
	public void generatePomByXml(String path,String xmlFile);
	
	/**
	 * 将baseSrc中的路径拷贝到路径中
	 */
	public void copyBaseSrc(String path,String artifactId);

	void copyBaseView(String path, String artifactId);

	/**
	 * 生成springboot的Application程序
	 * @param path
	 * @param artifactId
	 */
	public void generateApplicationConfig(String path,String groupId,String artifactId);

}
