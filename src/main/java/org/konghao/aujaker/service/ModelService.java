package org.konghao.aujaker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.konghao.aujaker.TestXml;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.stereotype.Service;

@Service
public class ModelService implements IModelService {

	@Override
	public void generateModels(String path, List<ClassEntity> entitys) {
		for(ClassEntity ce:entitys) {
			generateModel(path,ce);
		}
	}

	@Override
	public void generateModel(String path, ClassEntity entity) {
		File tf = new File(path);
		if(!tf.exists()) {
			tf.mkdirs();
		}
		path = path+"/src/main/java/"+CommonKit.packageToPath(entity.getPkgName());
		File f = new File(path);
		if(!f.exists()) f.mkdirs();
		
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(path+"/"+entity.getClassName()+".java"));
			generateTop(entity,ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(ps!=null) ps.close();
		}
	}

	/**
	 * 生成顶部信息，文件所在包，导入的包等信息
	 * 导入的包需要根据属性获取，并且还要生成和hibernate相关的包
	 * @param entity
	 * @param ps
	 */
	private void generateTop(ClassEntity entity, PrintStream ps) {
		//输出包名
		ps.println("package "+entity.getPkgName()+";");
		//输出两个空行
		newLine(ps);
		newLine(ps);
		
		generateImport(entity, ps);
		newLine(ps);
		
		generateClass(entity,ps);
		
	}
	
	private void generateClass(ClassEntity entity, PrintStream ps) {
		ps.println("/**");
		ps.println(" *");
		if(!CommonKit.isEmpty(entity.getCommet())) {
			ps.println(" *"+entity.getCommet());
		}
		if(!CommonKit.isEmpty(entity.getAuthor())) {
			ps.println(" * @author "+entity.getAuthor());
		} else {
			Properties properties = System.getProperties(); 
			ps.println(" * @author "+properties.getProperty("user.name"));
		}
		ps.println(" *\n */");
		ps.println("@Entity");
		ps.println("@Table(name=\""+entity.getTableName()+"\")");
		ps.println("public class "+entity.getClassName()+" {");
		newLine(ps);
		genereatorClassContent(entity.getProps(),ps);
		ps.println("}");
	}
	
	//生成属性
	private void genereatorClassContent(List<PropertiesBaseEntity> props, PrintStream ps) {
		List<String> properties = new ArrayList<String>();
		List<String> gs = new ArrayList<String>();
		StringBuffer psb = new StringBuffer();
		StringBuffer gssb = new StringBuffer();
		for(PropertiesBaseEntity pbe:props) {
			psb = new StringBuffer();
			gssb = new StringBuffer();
			/*完成了属性的存储*/
			if(!CommonKit.isEmpty(pbe.getCommet())) {
				psb.append("\t").append("/**\n")
				.append("\t * ").append(pbe.getCommet()).append("\n")
				.append("\t */\n");
			}
			if(pbe.isPk()) {
				psb.append("\t@Id\n");
				if(pbe.getPkType()==0) {
					psb.append("\t@GeneratedValue(strategy = GenerationType.AUTO)\n");
				} else {
					psb.append("\t@GeneratedValue(generator = \"UUID\")\n");
					psb.append("\t@GenericGenerator(name = \"UUID\",strategy = \"org.hibernate.id.UUIDGenerator\")\n");
				}
			}
			if(pbe.isLob()) {
				psb.append("\t@Lob\n");
			}
			if(!CommonKit.isEmpty(pbe.getColumnName())) {
				psb.append("\t@Column(name=\""+pbe.getColumnName()+"\")\n");
			}
			//生成属性代码
			psb.append("\tprivate ");
			String type = pbe.getType();
			if(type.indexOf(".")>=0) {
				type = type.substring(type.lastIndexOf(".")+1);
			}
			psb.append(type).append(" ").append(pbe.getName()).append(";\n");
			properties.add(psb.toString());
			
			/*为每个属性增加getter和setter方法*/
			//生成getter注释
			if(!CommonKit.isEmpty(pbe.getCommet())) {
				gssb.append("\t").append("/**\n")
				.append("\t * 获取").append(pbe.getCommet()).append("\n")
				.append("\t * @return ").append(pbe.getCommet()).append("\n")
				.append("\t */\n");
			}
			String getterP = generatorGet(pbe.getName());
			gssb.append("\tpublic ").append(type).append(" ").append(getterP).append("() {\n");
			gssb.append("\t\t return ").append(pbe.getName()).append(";\n");
			gssb.append("\t}\n\n");
			
			//生成setter注释
			if(!CommonKit.isEmpty(pbe.getCommet())) {
				gssb.append("\t").append("/**\n")
				.append("\t * 设置").append(pbe.getCommet()).append("\n")
				.append("\t * @param ").append(pbe.getCommet()).append("\n")
				.append("\t */\n");
			}
			String setterP = generatorSet(pbe.getName());
			gssb.append("\tpublic void ").append(setterP).append("(").append(type).append(" ").append(pbe.getName()).append(") { \n");
			gssb.append("\t\t this.").append(pbe.getName()).append(" = ").append(pbe.getName()).append(";\n");
			gssb.append("\t}\n\n");
			gs.add(gssb.toString());
		}
		
		for(String pro:properties) {
			ps.println(pro);
		}
		
		for(String gs1:gs) {
			ps.println(gs1);
		}
		
	}

	private String generatorSet(String name) {
		String n = name.substring(0,1).toUpperCase()+name.substring(1);
		return "set"+n;
	}

	private String generatorGet(String name) {
		String n = name.substring(0,1).toUpperCase()+name.substring(1);
		return "get"+n;
	}

	private void newLine(PrintStream ps) {
		ps.println("");
	}
	
	private void generateImport(ClassEntity entity,PrintStream ps) {
		//输出import
		List<PropertiesBaseEntity> ens = entity.getProps();
		//importhibernate
		ps.println("import javax.persistence.*;");
		for(PropertiesBaseEntity pbe:ens) {
			String type = pbe.getType();
			if(type.indexOf(".")>=0) {
				//说明这个类型需要导入
				ps.println("import "+type+";");
			}
			if(pbe.isPk()) {
				if(pbe.getPkType()==1) {
					ps.println("import org.hibernate.annotations.GenericGenerator;");
				}
			}
		}
		ps.println();
	}

	@Override
	public void generateModelsByProperties(String path, String file) {
		try {
			Properties prop = new Properties();
			prop.load(ModelService.class.getClassLoader().getResourceAsStream(file));
			List<ClassEntity> ents = generatorClassEntityByProp(prop);
			this.generateModels(path, ents);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			ce.setPkgName(pkg+".model");
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
	public void generateModelsByXml(String path, String file) {
		SAXReader reader = new SAXReader();
		try {
			Document d = reader.read(TestXml.class.getClassLoader().getResource("stu.xml"));
			Element root = d.getRootElement();
			Element ele = root.element("model");
			String pkgs = ele.attributeValue("package");
			List<Element> classes = ele.elements("class");
			List<ClassEntity> ces = generateClassesByXml(classes,pkgs);
			generateModels(path, ces);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
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
		System.out.println(props.size());
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
			ce.setTableName(e.attributeValue("comment"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("author"))) {
			ce.setTableName(e.attributeValue("author"));
		}
		if(!CommonKit.isEmpty(e.attributeValue("classShowName"))) {
			ce.setTableName(e.attributeValue("classShowName"));
		}
		return ce;
	}

}
