package org.konghao.aujaker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClassEntityService implements IClassEntityService {

	
	@Override
	public Map<String,Object> generateModelsByProperties(String file) {
		try {
			Map<String,Object> maps = new HashMap<String,Object>();
			Properties prop = new Properties();
			prop.load(ModelService.class.getClassLoader().getResourceAsStream(file));
			String artifactId = prop.getProperty("maven.artifactId");
			String groupId = prop.getProperty("maven.groupId");
			maps.put("artifactId", artifactId);
			maps.put("groupId", groupId);
			List<ClassEntity> ents = generatorClassEntityByProp(prop);
			maps.put("entity", ents);
			return maps;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<ClassEntity> generatorClassEntityByProp(Properties prop) {
		String pkg = prop.getProperty("package");
		String claKey = prop.getProperty("classes");
		String[] clas = claKey.split(",");
		List<ClassEntity> ens = new ArrayList<ClassEntity>();
		ClassEntity ce = null;
		for(String cla:clas) {
			String claProp = prop.getProperty("classes."+cla);
			ce = createClassEntity(claProp);
			ce.setPkgName(pkg);
			List<PropertiesBaseEntity> pbes = createClassProperties(prop,cla);
			ce.setProps(pbes);
			ens.add(ce);
		}
		return ens;
	}
	private List<PropertiesBaseEntity> createClassProperties(Properties prop,String cla) {
		String propValue = prop.getProperty(cla+".props");
		String[] props = propValue.split(",");
		List<PropertiesBaseEntity> pbes = new ArrayList<PropertiesBaseEntity>();
		for(String p:props) {
			String pValue = prop.getProperty(cla+".props."+p);
			PropertiesBaseEntity pbe = createClassProperty(pValue);
			pbes.add(pbe);
		}
		return pbes;
	}

	//#使用 name(属性名称)|columnName(字段名称)|type(数据类型)|isLob(是否是Lob)|commet注释|isPk(是否是主键)|pkType(主键类型)
	private PropertiesBaseEntity createClassProperty(String p) {
		String[] ps = p.split(",");
//		System.out.println(ps.length+p);
		PropertiesBaseEntity pbe = new PropertiesBaseEntity();
		if(!CommonKit.isEmpty(ps[0].trim())||!"-".equals(ps[0].trim())) {
			pbe.setName(ps[0]);
		}
		if(!CommonKit.isEmpty(ps[1])&&!"-".equals(ps[1].trim())) {
			pbe.setColumnName(ps[1].trim());
		}
		if(!CommonKit.isEmpty(ps[2])&&!"-".equals(ps[2].trim())) {
			pbe.setType(ps[2]);
		}
		if(!CommonKit.isEmpty(ps[3])&&!"-".equals(ps[3].trim())) {
			pbe.setLob(Boolean.parseBoolean(ps[3].trim()));;
		}
		if(!CommonKit.isEmpty(ps[4])&&!"-".equals(ps[4].trim())) {
			pbe.setCommet(ps[4].trim());;
		}
		if(!CommonKit.isEmpty(ps[5])&&!"-".equals(ps[5].trim())) {
			pbe.setPk(Boolean.parseBoolean(ps[5].trim()));
		}
		if(!CommonKit.isEmpty(ps[6])&&!"-".equals(ps[6].trim())) {
			pbe.setPkType(Integer.parseInt(ps[6].trim()));
		}
		return pbe;
	}

	//#使用 className(类名) | tableName(表名) |  comment(注释) | author(作者)
	private ClassEntity createClassEntity(String claProp) {
		String[] claProps = claProp.split(",");
		ClassEntity ce = new ClassEntity();
		if(!CommonKit.isEmpty(claProps[0])&&!"-".equals(claProps[0].trim())) {
			ce.setClassName(claProps[0].trim());
		}
		if(!CommonKit.isEmpty(claProps[1])&&!"-".equals(claProps[1].trim())) {
			ce.setTableName(claProps[1].trim());
		}
		if(!CommonKit.isEmpty(claProps[2])&&!"-".equals(claProps[2].trim())) {
			ce.setCommet(claProps[2].trim());
		}
		if(!CommonKit.isEmpty(claProps[3])&&!"-".equals(claProps[3].trim())) {
			ce.setAuthor(claProps[3].trim());
		}
		if(!CommonKit.isEmpty(claProps[4])&&!"-".equals(claProps[4].trim())) {
			ce.setClassShowName(claProps[4].trim());
		}
		return ce;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,Object> generateModelsByXml(String file) {
		SAXReader reader = new SAXReader();
		try {
			Map<String,Object> maps = new HashMap<String,Object>();
			Document d = reader.read(ModelService.class.getClassLoader().getResource(file));
			Element root = d.getRootElement();
			Element ele = root.element("model");
			Element maven = root.element("maven");
			String artifactId = maven.attributeValue("artifactId");
			String groupId = maven.attributeValue("groupId");
			maps.put("artifactId", artifactId);
			maps.put("groupId", groupId);
			String pkgs = ele.attributeValue("package");
			if(CommonKit.isEmpty(pkgs)) pkgs = groupId;
			List<Element> classes = ele.elements("class");
			List<ClassEntity> ces = generateClassesByXml(classes,pkgs);
			maps.put("entity", ces);
			return maps;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<ClassEntity> generateClassesByXml(List<Element> classes,String pkgs) {
		List<ClassEntity> ces = new ArrayList<ClassEntity>();
		ClassEntity ce = null;
		for(Element e:classes) {
			ce = generateClassByEle(e);
			ce.setPkgName(pkgs);
			List<PropertiesBaseEntity> pbes = generatePropByEle(e);
			ce.setProps(pbes);
			ces.add(ce);
		}
		return ces;
	}

	@SuppressWarnings("unchecked")
	private List<PropertiesBaseEntity> generatePropByEle(Element e) {
		List<Element> props = e.element("properties").elements("prop");
		//System.out.println(props.size());
		List<PropertiesBaseEntity> pbes = new ArrayList<PropertiesBaseEntity>();
		PropertiesBaseEntity pbe = null;
		for(Element prop:props) {
			pbe = new PropertiesBaseEntity();
			/*
			 * name:属性的名称[必填]
	 				columnName:字段名称，(可选，不填即是name)
	 				type:字段类型[必填]
	 				isLob:是否是二进制类型(可选，默认0,不是isLob)
	 				comment:属性的注释(可选，但尽可能填写)
	 				isPk:是否是主键(可选，默认是false，不是pk)
	 				pkType:主键类型(可选，默认是0，自动递增的int)
			 */
			if(!CommonKit.isEmpty(prop.attributeValue("name"))) {
				pbe.setName(prop.attributeValue("name"));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("type"))) {
				pbe.setType(prop.attributeValue("type"));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("columnName"))) {
				pbe.setColumnName(prop.attributeValue("columnName"));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("isLob"))) {
				pbe.setLob(Boolean.parseBoolean(prop.attributeValue("isLob")));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("comment"))) {
				pbe.setCommet(prop.attributeValue("comment"));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("isPk"))) {
				pbe.setPk(Boolean.parseBoolean(prop.attributeValue("isPk")));
			}
			if(!CommonKit.isEmpty(prop.attributeValue("pkType"))) {
				pbe.setPkType(Integer.parseInt(prop.attributeValue("pkType")));
			}
			pbes.add(pbe);
		}
		return pbes;
	}

	private ClassEntity generateClassByEle(Element e) {
		ClassEntity ce = new ClassEntity();
		//className="Student" tableName="t_stu" comment="学生信息" author="ynkonghao" classShowName="学生"
		if(!CommonKit.isEmpty(e.attributeValue("className"))) {
			ce.setClassName(e.attributeValue("className"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("tableName"))) {
			ce.setTableName(e.attributeValue("tableName"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("comment"))) {
			ce.setCommet(e.attributeValue("comment"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("author"))) {
			ce.setAuthor(e.attributeValue("author"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("classShowName"))) {
			ce.setClassShowName(e.attributeValue("classShowName"));
		}
		return ce;
	}

	@Override
	public Map<String,Object> generateModelsByProperties() {
		return generateModelsByProperties(FinalValue.PROP_FILE);
	}

	@Override
	public Map<String,Object> generateModelsByXml() {
		return generateModelsByXml(FinalValue.XML_FILE);
	}

}
