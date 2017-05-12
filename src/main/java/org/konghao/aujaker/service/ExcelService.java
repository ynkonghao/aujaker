package org.konghao.aujaker.service;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.kit.PinyinUtil;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ExcelService implements IExcelService {
	public static final Map<Integer,String> NUMS;
	static {
		NUMS = new HashMap<Integer,String>();
		for(int i=0;i<26;i++) {
			NUMS.put(i, Character.valueOf((char)('a'+i)).toString());
		}
	}
	@Override
	public Map<String, Object> xlsToEntity(String path) {
		return xlsToEntityByFile(ExcelService.class.getClassLoader().getResourceAsStream(path));
	}

	@Override
	public Map<String, Object> xlsToEntityByFile(String filePath) {
		try {
			return xlsToEntityByFile(new FileInputStream(new File(filePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, Object> xlsToEntityByFile(InputStream is) {
		Workbook wb = null;
		Map<String,Object> maps = new HashMap<String,Object>();
		List<ClassEntity> entitys = new ArrayList<ClassEntity>();
		maps.put(FinalValue.ARTIFACT_ID, FinalValue.EXCEL_ARTIFACTID);
		maps.put(FinalValue.GROUP_ID, FinalValue.EXCEL_GROUPID);
		maps.put(FinalValue.ENTITY, entitys);
		try {
			wb = WorkbookFactory.create(is);
			Iterator<Sheet> sheets = wb.sheetIterator();
			while(sheets.hasNext()) {
				ClassEntity ce = generateClassByXls(sheets.next());
				if(ce!=null) {
					ce.setPkgName(FinalValue.EXCEL_GROUPID);
					entitys.add(ce);
				}
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	private String snameToClassname(String sname) {
		String className = PinyinUtil.strAllFirst2Pinyin(sname);
		className = CommonKit.upcaseFirst(className);
		return className;
	}

	private ClassEntity generateClassByXls(Sheet sheet) {
		if(sheet.getLastRowNum()<=0) return null;
		String sname = sheet.getSheetName();
		String className = snameToClassname(sname);
		ClassEntity ce = new ClassEntity();
		ce.setClassName(className);
		ce.setClassShowName(sname);
		ce.setCommet(sname);
		ce.setTableName("t_"+PinyinUtil.strAllFirst2Pinyin(sname));
		List<PropertiesBaseEntity> pbes = generateProperties(sheet);
		ce.setProps(pbes);
		return ce;
	}
	
	private String getCellValue(Cell c) {
		String o = null;
		switch (c.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			o = ""; break;
		case Cell.CELL_TYPE_BOOLEAN:
			o = String.valueOf(c.getBooleanCellValue()); break;
		case Cell.CELL_TYPE_FORMULA:
			o = String.valueOf(c.getCellFormula()); break;
		case Cell.CELL_TYPE_NUMERIC:
			o = String.valueOf(c.getNumericCellValue()); break;
		case Cell.CELL_TYPE_STRING:
			o = c.getStringCellValue(); break;
		default:
			o = null;
			break;
		}
		return o;
	}
	

	private List<PropertiesBaseEntity> generateProperties(Sheet sheet) {
		List<PropertiesBaseEntity> pbes = new ArrayList<PropertiesBaseEntity>();
		//默认主键
		PropertiesBaseEntity pbe = new PropertiesBaseEntity();
		pbe.setColumnName("id");
		pbe.setCommet("主键");
		pbe.setLob(false);
		pbe.setName("id");
		pbe.setPk(true);
		pbe.setPkType(0);
		pbe.setType("Integer");
		pbes.add(pbe);
		Row row = sheet.getRow(0);
		for(Cell c:row) {
			pbe = new PropertiesBaseEntity();
			String content = getCellValue(c);
			//前缀
			String prefix = NUMS.get(c.getColumnIndex());
			String pname = null;
			if(!PinyinUtil.strFirst2Pinyin(content).equals(""))
				pname = prefix+"_"+PinyinUtil.strFirst2Pinyin(content);
			else
				pname=prefix;
			pbe.setColumnName(pname);
			pbe.setCommet(content);
			pbe.setLob(false);
			pbe.setName(pname);
			pbe.setPk(false);
			pbe.setPkType(0);
			pbe.setType("String");
			pbes.add(pbe);
		}
		return pbes;
	}
	
	@Override
	public void generateImpotTest(String destPath,String xlsPath) {
		generateImpotTest(destPath, new File(ExcelService.class.getClassLoader().getResource(xlsPath).getFile()));
	}

	@Override
	public void generateImpotTestByFile(String destPath,String xlsFilePath) {
		generateImpotTest(destPath, new File(xlsFilePath));
	}

	private void generateImpotTest(String destPath, File file) {
		try {
			Workbook wb = null;
			wb = WorkbookFactory.create(new FileInputStream(file));
			String npath = destPath+"/"+FinalValue.EXCEL_ARTIFACTID;
			//拷贝文件
			FileUtils.copyFile(file,
					new File(npath+"/src/main/resources/"+file.getName()));
			npath = npath+"/src/test/java/"+CommonKit.packageToPath(FinalValue.EXCEL_GROUPID);
			File f = new File(npath);
			if(!f.exists()) f.mkdirs();
			StringBuffer sb = new StringBuffer();
			sb.append("package com.zzsharing.aujaker;\n\n");
			sb.append("import com.zzsharing.aujaker.model.*;\n");
			sb.append("import com.zzsharing.aujaker.service.*;\n");
			sb.append("import com.zzsharing.aujaker.util.ExcelKit;\n");
			sb.append("import org.apache.poi.ss.usermodel.*;\n");
			sb.append("import org.junit.*;\n");
			sb.append("import org.junit.runner.RunWith;\n");
			sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
			sb.append("import org.springframework.boot.test.context.SpringBootTest;\n");
			sb.append("import org.springframework.test.context.junit4.SpringRunner;\n");
			sb.append("import java.util.List;\n");
			sb.append("import java.util.Map;\n\n");

			sb.append("@RunWith(SpringRunner.class)\n");
			sb.append("@SpringBootTest\n");
			sb.append("public class ExcelImportTest {\n\n");

			sb.append("\tprivate Workbook wb;\n\n");
			sb.append(generateAutoWire(wb));
			sb.append("\t@Before\n");
			sb.append("\tpublic void before() throws Exception {\n");
			sb.append("\t\twb = WorkbookFactory.create(ExcelImportTest.class.getClassLoader().getResourceAsStream(\""+file.getName()+"\"));\n");
			sb.append("\t}\n\n");

			sb.append(generateImport(wb));

			sb.append("}\n");
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(npath+"/ExcelImportTest.java"), "utf-8"));
				bw.write(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(bw!=null) bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	private String generateImport(Workbook wb) {
		StringBuffer sb = new StringBuffer();
		Iterator<Sheet> sheets = wb.sheetIterator();
		while(sheets.hasNext()) {
			Sheet sheet = sheets.next();
			if(sheet.getLastRowNum()<=0) continue;
			String cname = snameToClassname(sheet.getSheetName());
			sb.append("\t@Test\n");
			sb.append("\tpublic void testImport").append(cname).append("() {\n");
			sb.append("\t\tSheet sheet = wb.getSheet(\""+sheet.getSheetName()+"\");\n");
			sb.append("\t\tMap<Integer,String> maps = ExcelKit.sheetToProperties(sheet);\n");
			sb.append("\t\tList<Object> objs = ExcelKit.initObj(\""+FinalValue.EXCEL_GROUPID+".model.\"+ExcelKit.sheetToClassName(sheet),sheet,maps);\n");
			sb.append("\t\tfor(Object obj:objs) {\n");
			sb.append("\t\t\t").append(CommonKit.lowcaseFirst(cname))
				.append("Service.add((").append(cname).append(")obj").append(");\n");
			sb.append("\t\t}\n");
			sb.append("\t}\n\n");
		}
		return sb.toString();
	}

	private String generateAutoWire(Workbook wb) {
		StringBuffer sb = new StringBuffer();
		Iterator<Sheet> sheets = wb.sheetIterator();
		while(sheets.hasNext()) {
			Sheet sheet = sheets.next();
			if(sheet.getLastRowNum()<=0) continue;
			String cname = snameToClassname(sheet.getSheetName());
			sb.append("\t@Autowired\n");
			sb.append("\tprivate I"+cname+"Service ").append(CommonKit.lowcaseFirst(cname)).append("Service;\n\n");
		}
		return sb.toString();
	}

}
