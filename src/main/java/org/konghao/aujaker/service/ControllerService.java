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
		ps.println("@RequestMapping(\"/"+entity.getClassName().toLowerCase()+"\")");
		ps.println("public class "+entity.getClassName()+"Controller"+" {");
		newLine(ps);
		ps.println("\t@Autowired");
		ps.println("\tprivate I"+entity.getClassName()+"Service "+entity.getClassName().toLowerCase()+"Service;");
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
		ps.println("\t@RequestMapping(\"/list\")");
		ps.println("\tpublic String find(Model model, Integer page,HttpServletRequest request) {");
		ps.println("\t\tPage<"+entity.getClassName()+"> "+entity.getClassName().toLowerCase()+"s"
				+" = "+entity.getClassName().toLowerCase()+"Service.find(SimplePageBuilder.generate(page));");
		ps.println("\t\tmodel.addAttribute(\"datas\", "+
				entity.getClassName().toLowerCase()+"s);");
		ps.println("\t\treturn "+"\""+entity.getClassName().toLowerCase()+"/list\";");
		ps.println("\t}");
	}

	//TODO 所有主键类型都需要根据entity来处理,CommonKit.getPkType(ce)可以获取主键类型，
	//TODO 获取变量的名称不能直接使用toLowerCase,如果数据类型是StudentLesson名称就不对了!
	private void generateUpdatePost(ClassEntity entity, PrintStream ps) {
		
		ps.println("\t@RequestMapping(value=\"/update/{id}\",method=RequestMethod.POST)");
		ps.println("\tpublic String update(@PathVariable int id,"+entity.getClassName()+" "+CommonKit.lowcaseFirst(entity.getClassName())+") {");
		ps.println("\t\ttry {");
		ps.println("\t\t\t"+entity.getClassName()+" t"+entity.getClassName().toLowerCase()
				+" = "+entity.getClassName().toLowerCase()+"Service.load(id);");
		ps.println("\t\t\tBeanUtils.copyProperties(t"+entity.getClassName().toLowerCase()+", "+
				entity.getClassName().toLowerCase()+");");
		ps.println("\t\t\t"+entity.getClassName().toLowerCase()+"Service.update(t"+entity.getClassName().toLowerCase()+");");
		ps.println("\t\t} catch (IllegalAccessException e) {");
		ps.println("\t\t\te.printStackTrace();");
		ps.println("\t\t} catch (InvocationTargetException e) {");
		ps.println("\t\t\te.printStackTrace();");
		ps.println("\t\t\t}");
		ps.println("\t\treturn "+"\"redirect:/"+entity.getClassName().toLowerCase()+"/list\";");
		ps.println("\t}");
	}

	private void generateUpdateGet(ClassEntity entity, PrintStream ps) {
		ps.println("\t@RequestMapping(value=\"/update/{id}\",method=RequestMethod.GET)");
		ps.println("\tpublic String update(@PathVariable int id,Model model) {");
		ps.println("\t\t"+entity.getClassName()+" "+entity.getClassName().toLowerCase()+" "+
		"= "+entity.getClassName().toLowerCase()+"Service.load(id);");
		ps.println("\t\tmodel.addAttribute(\""+entity.getClassName().toLowerCase()+"\","+entity.getClassName().toLowerCase()+");");
		ps.println("\t\treturn \""+entity.getClassName().toLowerCase()+"/update\";");
		ps.println("\t}");
	}

	private void generateDelete(ClassEntity entity, PrintStream ps) {
		ps.println("\t@RequestMapping(\"/delete/{id}\")");
		ps.println("\tpublic String delete(@PathVariable int id) {");
		ps.println("\t\t"+entity.getClassName().toLowerCase()+"Service.delete(id);");
		ps.println("\t\treturn \"redirect:/"+entity.getClassName().toLowerCase()+"/list\";");
		ps.println("\t}");
	}

	private void generateShow(ClassEntity entity, PrintStream ps) {
		ps.println("\t@RequestMapping(\"/{id}\")");
		ps.println("\tpublic String show(@PathVariable int id,Model model) {");
		ps.println("\t\t"+entity.getClassName()+" "+entity.getClassName().toLowerCase()+
				" "+"= "+entity.getClassName().toLowerCase()+"Service.load(id);");
		ps.println("\t\tmodel.addAttribute("+"\""+entity.getClassName().toLowerCase()+"\","+" "+
				entity.getClassName().toLowerCase()+")"+";");
		ps.println("\t\treturn "+"\""+entity.getClassName().toLowerCase()+"/show"+"\";");
		ps.println("\t}");
	}

	private void nenetateAddPost(ClassEntity entity, PrintStream ps) {
		ps.println("\t@RequestMapping(value=\"/add\",method=RequestMethod.POST)");
		ps.println("\tpublic String add("+entity.getClassName()+" "+entity.getClassName().toLowerCase()+",Model model) {");
		ps.println("\t\t"+entity.getClassName().toLowerCase()+"Service.add("+entity.getClassName().toLowerCase()+");");
		ps.println("\t\treturn \""+"redirect:/"+entity.getClassName().toLowerCase()+"/list\";");
		ps.println("\t}");
	}

	private void generateAddGet(ClassEntity entity, PrintStream ps) {
		ps.println("\t@RequestMapping(value=\"/add\",method=RequestMethod.GET)");
		ps.println("\tpublic String add(Model model) {");
		ps.println("\t\t"+entity.getClassName()+" "+entity.getClassName().toLowerCase()+" "+"="+" "
		+"new"+" "+entity.getClassName()+"();");
		ps.println("\t\tmodel.addAttribute("+"\""+entity.getClassName().toLowerCase()+"\""+","+entity.getClassName().toLowerCase()+");");
		ps.println("\t\treturn "+"\""+entity.getClassName().toLowerCase()+"/add"+"\";");
		ps.println("\t}");
	}

	private void newLine(PrintStream ps) {
		ps.println("");
	}


	private void generateImport(ClassEntity entity,PrintStream ps,String artifactId,String groupId) {
		ps.println("import java.lang.reflect.InvocationTargetException;");
		ps.println("import javax.servlet.http.HttpServletRequest;");
		ps.println("import org.apache.commons.beanutils.BeanUtils;");
		ps.println("import org.konghao.reposiotry.kit.SimplePageBuilder;");
		ps.println("import "+groupId+".model."+entity.getClassName()+";");
		ps.println("import "+groupId+".service"+".I"+entity.getClassName()+"Service"+";");
		ps.println("import org.springframework.beans.factory.annotation.Autowired;");
		ps.println("import org.springframework.data.domain.Page;");
		ps.println("import org.springframework.stereotype.Controller;");
		ps.println("import org.springframework.ui.Model;");
		ps.println("import org.springframework.web.bind.annotation.PathVariable;");
		ps.println("import org.springframework.web.bind.annotation.RequestMapping;");
		ps.println("import org.springframework.web.bind.annotation.RequestMethod;");
		
	
	}

}
