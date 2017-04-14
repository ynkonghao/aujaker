package org.konghao.aujaker;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class TestXml {
	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		try {
			Document d = reader.read(TestXml.class.getClassLoader().getResource("stu.xml"));
			Element root = d.getRootElement();
			Element ele = root.element("model");
			List<Element> classes = ele.elements("class");
			System.out.println(classes.size());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
