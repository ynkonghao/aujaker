package org.konghao.aujaker.service;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 基于excel的版本，读取excel的数据，根据第一行来创建类和属性，
 * 所有的属性都以a_xxx b_xxx c_xxx来命名，第一个是a。第二个是b，以此类推
 * @author konghao
 *
 */
public interface IExcelService {
	/**
	 * 根据excel来生成实体对象
	 * @param path
	 * @return
	 */
	public Map<String,Object> xlsToEntity(String path);

	/**
	 * 根据excel来生成实体对象
	 * @param filePath
	 * @return
	 */
	Map<String,Object> xlsToEntityByFile(String filePath);
	
	/**
	 * 根据Excel生成Test的测试类
	 */
	public void generateImpotTest(String destPath,String xlsPath);

	/**
	 * 根据Excel生成Test的测试类
	 */
	public void generateImpotTestByFile(String destPath,String xlsFilePath);
}
