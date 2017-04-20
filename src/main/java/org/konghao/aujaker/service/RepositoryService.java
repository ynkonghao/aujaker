package org.konghao.aujaker.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.konghao.aujaker.kit.CommonKit;
import org.konghao.aujaker.model.ClassEntity;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService implements IRepositoryService {
	private final static String BASE_URL = "baseSrc";

	@Override
	public void copyBaseSrc(String path) {
		try {
			File f = new File(
					RepositoryService.class.getClassLoader().getResource(BASE_URL).getFile());
			File[] files = f.listFiles();
			for(File file:files) {
				String name = file.getName();
				File dest = new File(path+"/src/main/java/"+name);
				if(file.isFile()) {
					if(!dest.exists()) dest.createNewFile();
					FileUtils.copyFile(file, dest);
				} else {
					if(!dest.exists()) dest.mkdirs();
					FileUtils.copyDirectory(file, dest);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateRepository(List<ClassEntity> entities, String path) {
		for(ClassEntity e:entities) {
			generateRepository(e, path);
		}
	}
	

	@Override
	public void generateRepository(ClassEntity entity, String path) {
	}

}
