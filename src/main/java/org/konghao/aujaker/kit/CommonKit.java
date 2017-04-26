package org.konghao.aujaker.kit;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.PropertiesBaseEntity;
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
	/**
	 * 获得主键类型
	 * @param ce
	 * @return
	 */
	public static String getPkType(ClassEntity ce) {
		for(PropertiesBaseEntity pbe:ce.getProps()) {
			if(pbe.isPk()) {
				return pbe.getType();
			}
		}
		//默认是Integer
		return "Integer";
	}
	
	public static String getObjType(String type) {
		if(type.equals("int")) {
			return "Integer";
		} else if(type.equals("double")) {
			return "Double";
		} else if(type.equals("float")) {
			return "Float";
		} else if(type.equals("char")) {
			return "Character";
		} else if(type.equals("byte")) {
			return "Byte";
		} else if(type.equals("short")) {
			return "Short";
		} else if(type.equals("boolean")) {
			return "Boolean";
		} else if(type.equals("long")) {
			return "Long";
		}
		return type;
	}
	
	/**
	 * 生成变量名称，把第一个字母改成小写
	 * @param ce
	 * @return
	 */
	public static String generateVarName(ClassEntity ce) {
		String cname = ce.getClassName();
		cname = cname.substring(0,1).toLowerCase()+cname.substring(1);
		return cname;
	}
	
	public static String lowcaseFirst(String val) 
	{
		return val.substring(0,1).toLowerCase()+val.substring(1);
	}
	
	public static String generateTestPath(String path,String artifactId,ClassEntity ce,String type) {
		String npath = path+"/"+artifactId;
		npath = npath+"/src/test/java/"+CommonKit.packageToPath(ce.getPkgName())+"/"+lowcaseFirst(type);
		File f = new File(npath);
		if(!f.exists())
			f.mkdirs();
		return npath;
	}
	
	public static String generatePath(String path,String artifactId,ClassEntity entity,String fun) {
		String npath = path+"/"+artifactId;
		npath = npath+"/src/main/java/"+CommonKit.packageToPath(entity.getPkgName())+"/"+fun;
		File f = new File(npath);
		if(!f.exists())
			f.mkdirs();
		return npath;
	}
}
