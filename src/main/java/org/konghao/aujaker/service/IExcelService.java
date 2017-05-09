package org.konghao.aujaker.service;

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
}
