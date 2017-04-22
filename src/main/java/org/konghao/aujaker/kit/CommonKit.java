package org.konghao.aujaker.kit;

import java.io.IOException;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.konghao.aujaker.service.ModelService;


/**
 * 通用工具集合
 * @author konghao
 *
 */
public class CommonKit {
	/**
	 * 将包名转换为路径名称
	 * @param pkg
	 * @return
	 */
	public static String packageToPath(String pkg) {
		return pkg.replace(".", "/");
	}
	
	public static boolean isEmpty(Object obj) {
		if(obj==null) return true;
		if(obj instanceof String) {
			return ((String) obj).trim().equals("");
		}
		if(obj instanceof Integer) {
			return (Integer)obj==0;
		}
		return false;
	}
	
	public static Properties readProperties(String file) {
		try {
			Properties prop = new Properties();
			prop.load(CommonKit.class.getClassLoader().getResourceAsStream(file));
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Document readXml(String file) {
		try {
			SAXReader reader = new SAXReader();
			Document d = reader.read(ModelService.class.getClassLoader().getResource(file));
			return d;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据properties文件读取artifactId
	 * @param prop
	 * @return
	 */
	public static String readArtifactIdByProp(String prop) {
		Properties props = readProperties(prop);
		return props.getProperty("maven.artifactId");
	}
	
	public static String readGroupIdByProp(String prop) {
		Properties props = readProperties(prop);
		return props.getProperty("maven.groupId");
	}
	
	public static String readArtifactIdByXml(String xml) {
		Document doc = readXml(xml);
		Element root = doc.getRootElement();
		Element maven = root.element("maven");
		String artifactId = maven.attributeValue("artifactId");
		return artifactId;
	}
	
	public static String readGroupIdByXml(String xml) {
		Document doc = readXml(xml);
		Element root = doc.getRootElement();
		Element maven = root.element("maven");
		String groupId = maven.attributeValue("groupId");
		return groupId;
	}
}
