package org.konghao.aujaker.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.springframework.stereotype.Service;

@Service
public class ControllerService implements IControllerService {

	@Override
	public void generateControllers(String path, Map<String, Object> maps) {
		String artifactId = (String)maps.get(FinalValue.ARTIFACT_ID);
		String groupId = (String)maps.get(FinalValue.GROUP_ID);
		@SuppressWarnings("unchecked")
		List<ClassEntity> entitys = (List<ClassEntity>)maps.get(FinalValue.ENTITY);
		for(ClassEntity ce:entitys) {
			generateController(path,ce,artifactId,groupId);
		}
	}

	@Override
	public void generateController(String path, ClassEntity entity, String artifactId,String groupId) {
		path = CommonKit.generatePath(path, artifactId, entity, "controller");
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(path+"/"+entity.getClassName()+"Controller"+".java"));
			generateTop(entity,ps,artifactId,groupId);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(ps!=null) ps.close();
		}
	}
	
	private void generateTop(ClassEntity entity, PrintStream ps,String artifactId,String groupId) {
		//输出包名
		ps.println("package "+groupId+".controller;");
	
		newLine(ps);
		
		generateImport(entity, ps,artifactId,groupId);
		newLine(ps);
		
		generateClass(entity,ps);
		
	}
	
	private void generateClass(ClassEntity entity, PrintStream ps) {
		ps.println("@Controller");
		ps.println("@RequestMapping(\"/"+CommonKit.generateVarName(entity)+"\")");
		ps.println("public class "+entity.getClassName()+"Controller"+" {");
		newLine(ps);
		ps.println("\t@Autowired");
		ps.println("\tprivate I"+entity.getClassName()+"Service "+CommonKit.generateVarName(entity)+"Service;");
		newLine(ps);
		generateAddGet(entity,ps);
		newLine(ps);
		nenetateAddPost(entity,ps);
		newLine(ps);
		generateShow(entity,ps);
		newLine(ps);
		generateDelete(entity,ps);
		newLine(ps);
		generateUpdateGet(entity,ps);
		newLine(ps);
		generateUpdatePost(entity,ps);
		newLine(ps);
		generateFind(entity,ps);
		ps.println("}");
	}
	
	
	private void generateFind(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(\"/list\")").append("\r\n");
		buffer.append("\tpublic String find(Model model,Integer page,HttpServletRequest request) {").append("\r\n");
		buffer.append("\t\tPage<"+entity.getClassName()+"> "+CommonKit.generateVarName(entity)+"s"
				+" = "+CommonKit.generateVarName(entity)+"Service.find(SimplePageBuilder.generate(page));").append("\r\n");
		buffer.append("\t\tmodel.addAttribute(\"datas\","+
				CommonKit.generateVarName(entity)+"s);").append("\r\n");
		buffer.append("\t\treturn "+"\""+CommonKit.generateVarName(entity)+"/list\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	//TODO 所有主键类型都需要根据entity来处理,CommonKit.getPkType(ce)可以获取主键类型，
	//TODO 获取变量的名称不能直接使用toLowerCase,如果数据类型是StudentLesson名称就不对了!
	private void generateUpdatePost(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(value=\"/update/{id}\",method=RequestMethod.POST)").append("\r\n");
		buffer.append("\tpublic String update(@PathVariable "+CommonKit.getPkType(entity)+" id,"+entity.getClassName()+" "+CommonKit.lowcaseFirst(entity.getClassName())+") {").append("\r\n");
		buffer.append("\t\ttry {").append("\r\n");
		buffer.append("\t\t\t"+entity.getClassName()+" t"+CommonKit.generateVarName(entity)
		+" = "+CommonKit.generateVarName(entity)+"Service.load(id);").append("\r\n");
		buffer.append("\t\t\tBeanUtils.copyProperties(t"+CommonKit.generateVarName(entity)+", "+
				CommonKit.generateVarName(entity)+");").append("\r\n");
		buffer.append("\t\t\t"+CommonKit.generateVarName(entity)+"Service.update(t"+CommonKit.generateVarName(entity)+");").append("\r\n");
		buffer.append("\t\t} catch (IllegalAccessException e) {").append("\r\n");
		buffer.append("\t\t\te.printStackTrace();").append("\r\n");
		buffer.append("\t\t} catch (InvocationTargetException e) {").append("\r\n");
		buffer.append("\t\t\te.printStackTrace();").append("\r\n");
		buffer.append("\t\t}").append("\r\n");
		buffer.append("\t\treturn "+"\"redirect:/"+CommonKit.generateVarName(entity)+"/list\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void generateUpdateGet(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(value=\"/update/{id}\",method=RequestMethod.GET)").append("\r\n");
		buffer.append("\tpublic String update(@PathVariable "+CommonKit.getPkType(entity)+" id,Model model) {").append("\r\n");
		buffer.append("\t\t"+entity.getClassName()+" "+CommonKit.generateVarName(entity)+" "+
		"= "+CommonKit.generateVarName(entity)+"Service.load(id);").append("\r\n");
		buffer.append("\t\tmodel.addAttribute(\""+CommonKit.generateVarName(entity)+"\","+CommonKit.generateVarName(entity)+");").append("\r\n");
		buffer.append("\t\treturn \""+CommonKit.generateVarName(entity)+"/update\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void generateDelete(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(\"/delete/{id}\")").append("\r\n");
		buffer.append("\tpublic @ResponseBody String delete(@PathVariable "+CommonKit.getPkType(entity)+" id) {").append("\r\n");
		buffer.append("\t\t"+CommonKit.generateVarName(entity)+"Service.delete(id);").append("\r\n");
		buffer.append("\t\treturn \"1\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void generateShow(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(\"/{id}\")").append("\r\n");
		buffer.append("\tpublic String show(@PathVariable "+CommonKit.getPkType(entity)+" id,Model model) {").append("\r\n");
		buffer.append("\t\t"+entity.getClassName()+" "+CommonKit.generateVarName(entity)+
				" "+"= "+CommonKit.generateVarName(entity)+"Service.load(id);").append("\r\n");
		buffer.append("\t\tmodel.addAttribute("+"\""+CommonKit.generateVarName(entity)+"\","+" "+
				CommonKit.generateVarName(entity)+")"+";").append("\r\n");
		buffer.append("\t\treturn "+"\""+CommonKit.generateVarName(entity)+"/show"+"\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void nenetateAddPost(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(value=\"/add\",method=RequestMethod.POST)").append("\r\n");
		buffer.append("\tpublic String add("+entity.getClassName()+" "+CommonKit.generateVarName(entity)+",Model model) {").append("\r\n");
		buffer.append("\t\t"+CommonKit.generateVarName(entity)+"Service.add("+CommonKit.generateVarName(entity)+");").append("\r\n");
		buffer.append("\t\treturn \""+"redirect:/"+CommonKit.generateVarName(entity)+"/list\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void generateAddGet(ClassEntity entity, PrintStream ps) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\t@RequestMapping(value=\"/add\",method=RequestMethod.GET)"+System.getProperty("line.separator"));
		buffer.append("\tpublic String add(Model model) {\r\n");
		buffer.append("\t\t"+entity.getClassName()+" "+CommonKit.generateVarName(entity)+" "+"="+" "
		+"new"+" "+entity.getClassName()+"();").append("\r\n");
		buffer.append("\t\tmodel.addAttribute("+"\""+CommonKit.generateVarName(entity)+"\""+","+CommonKit.generateVarName(entity)+");").append("\r\n");
		buffer.append("\t\treturn "+"\""+CommonKit.generateVarName(entity)+"/add"+"\";").append("\r\n");
		buffer.append("\t}");
		ps.println(buffer);
	}

	private void newLine(PrintStream ps) {
		ps.println("");
	}


	private void generateImport(ClassEntity entity,PrintStream ps,String artifactId,String groupId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("import java.lang.reflect.InvocationTargetException;").append("\r\n");
		buffer.append("import javax.servlet.http.HttpServletRequest;").append("\r\n");
		buffer.append("import org.apache.commons.beanutils.BeanUtils;").append("\r\n");
		buffer.append("import org.konghao.reposiotry.kit.SimplePageBuilder;").append("\r\n");
		buffer.append("import "+groupId+".model."+entity.getClassName()+";").append("\r\n");
		buffer.append("import "+groupId+".service"+".I"+entity.getClassName()+"Service"+";").append("\r\n");
		buffer.append("import org.springframework.beans.factory.annotation.Autowired;").append("\r\n");
		buffer.append("import org.springframework.data.domain.Page;").append("\r\n");
		buffer.append("import org.springframework.stereotype.Controller;").append("\r\n");
		buffer.append("import org.springframework.ui.Model;").append("\r\n");
		buffer.append("import org.springframework.web.bind.annotation.PathVariable;").append("\r\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMapping;").append("\r\n");
		buffer.append("import org.springframework.web.bind.annotation.RequestMethod;").append("\r\n");
		buffer.append("import org.springframework.web.bind.annotation.ResponseBody;").append("\r\n");
		ps.println(buffer);
	}

}
