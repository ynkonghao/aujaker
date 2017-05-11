package com.zzsharing.aujaker.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by konghao on 2017/5/11 0011.
 */
public class ExcelKit {
    public static final Map<Integer,String> NUMS;
    static {
        NUMS = new HashMap<Integer,String>();
        for(int i=0;i<26;i++) {
            NUMS.put(i, Character.valueOf((char)('a'+i)).toString());
        }
    }

    public static String sheetToClassName(Sheet sheet) {
        String sname = sheet.getSheetName();
        String className = PinyinUtil.strAllFirst2Pinyin(sname);
        className = Commonkit.upcaseFirst(className);
        return className;
    }

    public static Object SheetToObj(Sheet sheet,String pkg) {
        try {
            String cname = sheetToClassName(sheet);
            Object obj = Class.forName(pkg+"."+cname).newInstance();
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCellValue(Cell c) {
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

    public static String cellToProperties(Cell c) {
        String content = getCellValue(c);
        //前缀
        String prefix = NUMS.get(c.getColumnIndex());
        String pname = null;
        if(!PinyinUtil.strFirst2Pinyin(content).equals(""))
            pname = prefix+"_"+PinyinUtil.strFirst2Pinyin(content);
        else
            pname=prefix;
        return pname;
    }

    /**
     * 把所有的sheet转换为属性
     * @return
     */
    public static Map<Integer,String> sheetToProperties(Sheet sheet) {
        Row row = sheet.getRow(0);
        Map<Integer,String> maps = new HashMap<Integer,String>();
        for(Cell c:row) {
            maps.put(c.getColumnIndex(),cellToProperties(c));
        }
        return maps;
    }

    public static List<Object> initObj(String name, Sheet sheet, Map<Integer, String> maps) {
        try {
            List<Object> objs = new ArrayList<Object>();
            for(int i=1;i<=sheet.getLastRowNum();i++) {
                Object obj = Class.forName(name).newInstance();
                Row row = sheet.getRow(i);
                for(Cell c:row) {
                    int index = c.getColumnIndex();
                    String proName = maps.get(index);
                    String proValue = getCellValue(c);
                    BeanUtils.copyProperty(obj,proName,proValue);
                }
                objs.add(obj);
            }
            return objs;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
