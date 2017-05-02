package org.konghao.aujaker.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.konghao.aujaker.model.FinalValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService implements IProjectService {
	
	@Autowired
	private IModelService modelService;
	@Autowired
	private IConfigService configService;
	@Autowired
	private IRepositoryService repositoryService;
	@Autowired
	private IBusinessService businessService;
	@Autowired
	private IClassEntityService classEntityService;
	@Autowired
	private ICheckFileService checkFileService;
	@Autowired
	private IControllerService controllerService;
	@Autowired
	private ITestTemplatesService testTemplatesService;
	
	@Override
	public void initProject(String path) {
		Map<String,Object> maps = classEntityService.generateModelsByXml();
		checkFileService.checkXmlFile();
		modelService.generateModels(path, maps);
		configService.generateApplicationPropertiesByXml(path, "aujaker.xml");
		configService.generatePomByXml(path, "aujaker.xml");
		configService.copyBaseSrc(path,(String)maps.get(FinalValue.ARTIFACT_ID));
		configService.generateApplicationConfig(path, (String)maps.get(FinalValue.GROUP_ID), (String)maps.get(FinalValue.ARTIFACT_ID));
		repositoryService.generateRepository(maps, path);
		businessService.generateService(maps, path);
		controllerService.generateControllers(path, maps);
		testTemplatesService.generateTestTemplate(path, maps,ITestTemplatesService.REPOS_TYPE);
		this.mvnPackage(path, (String)maps.get(FinalValue.GROUP_ID));
	}

	@Override
	public void mvnPackage(String path,String artifactId) {
		try {
			String mpath = path+"/"+artifactId;
			Runtime runtime = Runtime.getRuntime();
			String exec = null;
			if(path.charAt(1)==':') {
				//说明是win的系统
				String disk = path.substring(0,2);
				exec = "cmd /c "+disk+" && cd "+mpath+" && mvn clean package";
			} else {
				exec = "/c cd "+mpath+" && mvn clean package";
			}
			Process process = runtime.exec(exec);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			while((str=br.readLine())!=null) {
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

}
