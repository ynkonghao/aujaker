package org.konghao.aujaker.service;

import java.io.File;

public interface ICheckFileService {
	/**
	 * @param path xml上传文件的路径
	 * */
	public void checkXmlFile(String path);

	void checkXmlFileByUpload(File file);
	
	public void checkXmlFile();
	
//	public void checkPropertiesFile(String path);
}
