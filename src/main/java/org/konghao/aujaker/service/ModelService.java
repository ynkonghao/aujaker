package org.konghao.aujaker.service;

import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class ModelService implements IModelService {

	@SuppressWarnings("unchecked")
	@Override
	public void generateModels(String path, Map<String,Object> maps) {
		String artifactId = (String)maps.get(FinalValue.ARTIFACT_ID);
		List<ClassEntity> entitys = (List<ClassEntity>)maps.get(FinalValue.ENTITY);
		for(ClassEntity ce:entitys) {
			generateModel(path,ce,artifactId);
		}
	}

	@Override
	public void generateModel(String path, ClassEntity entity,String artifactId) {
		path = CommonKit.generatePath(path, artifactId, entity, "model");
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(path+"/"+entity.getClassName()+".java"), true, "UTF-8");
			generateTop(entity,ps);
		} catch (Exception e) {
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
		ps.println("package "+entity.getPkgName()+".model;");
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
			ps.println(" * "+entity.getCommet());
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
				.append("\t * @param ").append(pbe.getName()).append(" ").append(pbe.getCommet()).append("\n")
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
}
