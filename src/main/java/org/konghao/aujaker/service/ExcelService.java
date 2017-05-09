package org.konghao.aujaker.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.kit.PinyinUtil;
import org.konghao.aujaker.model.ClassEntity;
import org.konghao.aujaker.model.FinalValue;
import org.konghao.aujaker.model.PropertiesBaseEntity;
import org.springframework.stereotype.Service;

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
		Workbook wb = null;
		Map<String,Object> maps = new HashMap<String,Object>();
		List<ClassEntity> entitys = new ArrayList<ClassEntity>();
		maps.put(FinalValue.ARTIFACT_ID, FinalValue.EXCEL_ARTIFACTID);
		maps.put(FinalValue.GROUP_ID, FinalValue.EXCEL_GROUPID);
		maps.put(FinalValue.ENTITY, entitys);
		try {
			wb = WorkbookFactory.create(ExcelService.class.getClassLoader().getResourceAsStream(path));
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

	private ClassEntity generateClassByXls(Sheet sheet) {
		if(sheet.getLastRowNum()<=0) return null;
		String sname = sheet.getSheetName();
		String className = PinyinUtil.strAllFirst2Pinyin(sname);
		className = CommonKit.upcaseFirst(className);
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
	
	private String numToString(int num) {
		return null;
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

}
