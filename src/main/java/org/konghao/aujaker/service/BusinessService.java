package org.konghao.aujaker.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.springframework.stereotype.Service;

@Service
public class BusinessService implements IBusinessService {

	@SuppressWarnings("unchecked")
	@Override
	public void generateService(Map<String, Object> maps, String path) {
		for(ClassEntity ce:(List<ClassEntity>)maps.get(FinalValue.ENTITY)) {
			generateService(ce, path, (String)maps.get(FinalValue.ARTIFACT_ID));
		}
	}

	@Override
	public void generateService(ClassEntity entity, String path, String artifactId) {
		path = CommonKit.generatePath(path, artifactId, entity,"service");
		generateServiceInterface(entity,path);
		generateServiceImpl(entity,path);
	}
	/**
	 * 生成实现类，在add、update和delete方法上添加@Transactional的说明
	 * @param entity
	 * @param path
	 */
	private void generateServiceImpl(ClassEntity entity, String path) {
		String file = path+"/"+entity.getClassName()+"ServiceImpl.java";
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			StringBuffer sb = new StringBuffer();
			//生成package
			sb.append("package ").append(entity.getPkgName()).append(".service;\n\n");
			//生成import
			sb.append("import ").append(entity.getPkgName()).append(".repository.*;\n");
			sb.append("import ").append(entity.getPkgName()).append(".model.*;\n");
			sb.append("import java.util.List;\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.data.domain.Page;\n");
			sb.append("import org.springframework.data.domain.Pageable;\n");
			sb.append("import org.springframework.stereotype.Service;\n");
			sb.append("import org.springframework.transaction.annotation.Transactional;\n\n");
			//生成类
			sb.append("@Service\n");
			sb.append("public class ").append(entity.getClassName()).append("ServiceImpl ")
			  .append("implements I").append(entity.getClassName()).append("Service {\n");
			
			//生成Repository的注入
			String rname = CommonKit.generateVarName(entity)+"Repository";
			String pkType = CommonKit.getObjType(CommonKit.getPkType(entity));
			sb.append("\n").append("\t@Autowired\n");
			sb.append("\tprivate I").append(entity.getClassName()).append("Repository ")
			  .append(rname).append(";\n\n");
			
			//生成add方法
			sb.append("\t/**\n").append("\t * 添加").append(entity.getClassShowName()).append("对象\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Transactional\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic void add(").append(entity.getClassName()).append(" t) {\n");
			sb.append("\t\t").append(rname).append(".save(t);\n");
			sb.append("\t}\n");
			
			//生成update方法
			sb.append("\t/**\n").append("\t * 更新").append(entity.getClassShowName()).append("对象\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Transactional\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic void update(").append(entity.getClassName()).append(" t) {\n");
			sb.append("\t\t").append(rname).append(".save(t);\n");
			sb.append("\t}\n");
			
			//生成delete方法
			sb.append("\t/**\n").append("\t * 删除").append(entity.getClassShowName()).append("对象\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Transactional\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic void delete(").append(pkType).append(" id) {\n");
			sb.append("\t\t").append(rname).append(".delete(id);\n");
			sb.append("\t}\n");
			
			//生成load方法
			sb.append("\t/**\n").append("\t * 加载").append(entity.getClassShowName()).append("对象\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic "+entity.getClassName()+" load(").append(pkType).append(" id) {\n");
			sb.append("\t\t return ").append(rname).append(".findOne(id);\n");
			sb.append("\t}\n");
			
			
			//生成list方法
			sb.append("\t/**\n").append("\t * 获取所有的").append(entity.getClassShowName()).append("对象,不进行分页\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic List<"+entity.getClassName()+"> list() {\n");
			sb.append("\t\t return ").append(rname).append(".findAll();\n");
			sb.append("\t}\n");
			
			//生成find方法
			sb.append("\t/**\n").append("\t * 获取所有的").append(entity.getClassShowName()).append("对象,进行分页\n")
			  .append("\t * \n\t */\n");
			sb.append("\t@Override\n");
			sb.append("\tpublic Page<"+entity.getClassName()+"> find(Pageable page) {\n");
			sb.append("\t\t return ").append(rname).append(".findAll(page);\n");
			sb.append("\t}\n");
			
			sb.append("}\n");
			
			
			fw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw!=null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成接口
	 * @param entity
	 * @param path
	 */
	private void generateServiceInterface(ClassEntity entity, String path) {
		String file = path+"/I"+entity.getClassName()+"Service.java";
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			StringBuffer sb = new StringBuffer();
			//生成包
			sb.append("package ").append(entity.getPkgName()).append(".service;\n\n");
			//生成import
			sb.append("import org.konghao.service.IBaseService;\n");
			sb.append("import ").append(entity.getPkgName()).append(".model.").append(entity.getClassName()).append(";\n\n");
			//生成接口注释
			sb.append("/**\n");
			sb.append(" * ").append(entity.getClassShowName()).append("Service的实现\n");
			sb.append(" * 默认实现了CRUD方法，add:添加,update:更新,delete:删除,load:加载一个对象,list:列表对象不分页,find:分页列表对象\n");
			sb.append(" * @author ").append(entity.getAuthor()).append("\n * \n */\n");
			//生成接口类
			sb.append("public interface I").append(entity.getClassName()).append("Service ")
			  .append("extends IBaseService<").append(entity.getClassName()).append(",")
			  .append(CommonKit.getObjType(CommonKit.getPkType(entity))).append("> {\n\n}");
			
			fw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw!=null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
