package org.konghao.aujaker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.AujakerException;
import org.springframework.stereotype.Service;

@Service
public class CheckFileService implements ICheckFileService {

	public void checkXmlFile(String path) {
		try {
			SAXReader reader = new SAXReader();
			Document d = reader.read(CheckFileService.class.getClassLoader().getResource(path));
			Element roots = d.getRootElement();
			getNodes(roots);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void getNodes(Element node) {
		// System.out.println("--------------------");
		// 当前节点的名称、文本内容和属性
		// System.out.println("当前节点名称：" + node.getName());
		// System.out.println("当前节点的内容："+node.getTextTrim());
		List<Attribute> listAttr = node.attributes();
		for (Attribute attr : listAttr) {
			String name = attr.getName();
			String value = attr.getValue();
			// System.out.println("属性名称：" + name);
		}
		checkNodes(node);
		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodes(e);// 递归
		}
	}

	private void checkNodes(Element node) {

		Properties prop = CommonKit.readProperties("checkXmlFile.properties");
		List<String> nodeAttr = new ArrayList<>();
		List<Attribute> attrs = node.attributes();
		for (Attribute attr : attrs) {
			nodeAttr.add(attr.getName());
		}
		if (!prop.containsKey("@" + node.getName())) {
			return;
		}
		String attrName = prop.getProperty("@" + node.getName());
		String[] attrNames = attrName.split(",");

		for (int i = 0; i < attrNames.length; i++) {
			if (!nodeAttr.contains(attrNames[i])) {
				throw new AujakerException("文件不合法，节点属性不正确！");
			}
		}

		List<Element> listElement = node.elements();
		List<String> elements = new ArrayList<>();
		for (Element e : listElement) {
			elements.add(e.getName());
		}

		for (String attr : nodeAttr) {
			System.out.println("attr:" + attr);
		}

		String name = node.getName();
		// System.out.println("name:"+name);
		// System.out.println(prop.containsKey("-"+name));
		if (!prop.containsKey("-" + name)) {
			return;
		}

		String checkName = prop.getProperty("-" + name);
		String[] names = checkName.split(",");
		for (int i = 0; i < names.length; i++) {
			// System.out.println("names:"+names[i]);
			if (!elements.contains(names[i])) {
				throw new AujakerException("文件不合法，节点不正确！");
			}
		}
	}

	/*@Override
	public void checkPropertiesFile(String path) {
		Properties file = CommonKit.readProperties(path);
		Set<Object> keys = file.keySet();

		Properties check = CommonKit.readProperties("checkPropFile.properties");
		String[] checkKeys = check.getProperty("require").split(",");
		for (int i = 0; i < checkKeys.length; i++) {
			if (!keys.contains(checkKeys[i])) {
				throw new AujakerException("文件不合法");
			}
		}
		checkClass(file, check);
	}

	private void checkClass(Properties file, Properties check) {
		String[] classes = file.getProperty("classes").split(",");
		for (int i = 0; i < classes.length; i++) {
			String[] object = file.getProperty("classes." + classes[i]).split(",");
			if(object.length<5) throw new AujakerException("文件格式不合法");
			for (int j = 0; j < object.length; j++) {
				System.out.println(object[j]);
				if (object[0] == null || object[1] == null) {
					throw new AujakerException("文件格式不合法");
				}
			}
		
		}
	}*/

}
