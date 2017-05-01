package org.konghao.aujaker.service;

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
		repositoryService.generateRepository(maps, path);
		businessService.generateService(maps, path);
		controllerService.generateControllers(path, maps);
		testTemplatesService.generateTestTemplate(path, maps,ITestTemplatesService.REPOS_TYPE);
	}

}
